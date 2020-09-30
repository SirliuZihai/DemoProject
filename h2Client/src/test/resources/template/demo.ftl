{
    "userId":${userId},
    "age":${person.age},
    "time":${(time*1000)?number_to_datetime}
    "arrays":[
    <#list arrays as s>
        ${s}<#if !(s_index ==(arrays?size -1))>,</#if>
    </#list>
    ]
}