-- 多设备登录，三台，hash存储
-- key: Login_$username
-- value:
--      key:token
--      value:时间戳

local username = tostring(ARGV[1])
local token = tostring(ARGV[2])
--local timestamp = tonumber(ARGV[3])
local stringTime = tostring(ARGV[3])

--检查是否存在
local exists = redis.call('EXISTS', username)
if exists == 0 then
    redis.call('HSET', username, token, stringTime)
    return 0
end
local tokenCount = redis.call('HLEN', username)


while tokenCount >= 3 do
    local minTimestamp = stringTime
    local minToken = nil
    for k,v in pairs(redis.call('HGETALL', username)) do
        if v < minTimestamp then
            minTimestamp = v
            minToken = k
        end
    end
    redis.call('SET', minToken, minTimestamp)
    redis.call('HDEL', username, minToken)
    tokenCount = tokenCount - 1
end

redis.call('HSET', username, token, stringTime)

return 0