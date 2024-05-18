-- 传入loginKey和token与当前时间戳
-- 检查当前token是否存在，若存在检查时间是否超时，超时则删除，同时检查是否删除info 返回false
-- token存在且不超时更新当前时间戳，返回true

local hKey =  tostring(ARGV[1])
local key = hKey .. 'info'
local token = tostring(ARGV[2])
--local timestamp = tonumber(ARGV[3])

local now_time = redis.call('TIME')
local now = tonumber(now_time[1])

local oldTime = redis.call('HGET', hKey, token)
if oldTime then
--

    if oldTime+3600 > now then
--         未超时
        redis.call('HSET', hKey, token, now)
        return true
    end
end
--     超时或不存在
--     判断hash是否存在
redis.call('HDEL', hKey, token)
local exists = redis.call('EXISTS', hKey)
if exists == 0 then
    redis.call('DEL', key)
end
return false
