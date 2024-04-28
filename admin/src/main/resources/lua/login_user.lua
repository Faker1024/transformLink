-- 多设备登录，三台，hash存储
-- key: Login_$username
-- value:
--      key:token
--      value:时间戳

local hKey = tostring(ARGV[1])
local token = tostring(ARGV[2])
--local timestamp = tonumber(ARGV[3])
local stringTime = tostring(ARGV[3])

--检查是否存在
local exists = redis.call('EXISTS', hKey)
if exists == 0 then
    redis.call('HSET', hKey, token, stringTime)
    return 0
end
local tokenCount = redis.call('HLEN', hKey)


while tokenCount >= 3 do
    local minTimestamp = stringTime
    local minToken = nil
    for _, k in pairs(redis.call('HKEYS', hKey)) do
        local value = redis.call('HGET', hKey, k)
        if value < minTimestamp then
            minTimestamp =value
            minToken = k
        end
    end
    --redis.call('SET', minToken, minTimestamp)
    redis.call('HDEL', hKey, minToken)
    tokenCount = tokenCount - 1
end

redis.call('HSET', hKey, token, stringTime)

return 0