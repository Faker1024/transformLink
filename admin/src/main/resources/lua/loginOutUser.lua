local hKey = tostring(ARGV[1])
local token = tostring(ARGV[2])
local key = hKey .. 'info'


-- 删除之后检查hash是否存在，是则删除对应string
redis.call('HDEL', hKey, token)
local exists = redis.call('EXISTS', hKey)
if exists == 0 then
    redis.call('DEL', key)
end
return 1