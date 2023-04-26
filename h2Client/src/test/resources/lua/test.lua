local key = KEYS[1]
local expirTime = tonumber(ARGV[1])
local word = ARGV[2]
local plusTime = tonumber(ARGV[3])
local current = redis.call('HINCRBY', key,"times",1)
if(current>=2)
then
    redis.call('HMSET',key,"startTime",expirTime+plusTime)
    redis.call('PERSIST',key)
else
    redis.call('HMSET',key,"startTime",333)
    redis.call('HMSET',key,"SBST",word)
    redis.call('EXPIRE',key,expirTime)
end
return tonumber(current)