SELECT
	im_m.*,
	im_u.*
FROM
	im_message im_m,
	im_friend_dnexus im_f_d,
	im_user im_u
WHERE
	im_f_d._self = im_m._to
AND im_f_d._another = im_m._from
AND im_m._to = 'zhangsan'
AND im_m._from = 'lisi'
AND im_m._createTime > im_f_d._lastReadTime
AND im_u._userid = im_m._from
--查询我与对方的未读消息



SELECT
	im_f_g.*
FROM
	im_friend_group im_f_g
LEFT JOIN im_user im_u ON im_f_g._pertain = im_u._userid
WHERE
	im_u._userid = 'zhangsan'--我的id
--查询我的分组
	
SELECT
	im_u.*
FROM
	im_friend_dnexus im_f_d
LEFT JOIN im_friend_group im_f_g ON im_f_d._groupId = im_f_g._id
LEFT JOIN im_user im_u ON im_f_d._another = im_u._userid
WHERE
	im_f_g._pertain = 'zhangsan' --我的id
AND im_f_g._id = 'jiaren'--对应分组
--查询分组查询该组的所有好友

SELECT
	im_c_g.*
FROM
	im_chat_member im_c_m
LEFT JOIN im_chat_group im_c_g ON im_c_m._chatGroupId = im_c_g._id
WHERE
	im_c_m._memberId = 'lisi'--我的id
--查询我的所有群组聊天

	
	
	
SELECT
	im_m.*, im_u.*
FROM
	im_chat_member im_c_m
LEFT JOIN im_chat_group im_c_g ON im_c_m._chatGroupId = im_c_g._id
LEFT JOIN im_message im_m ON im_m._to = im_c_g._id
LEFT JOIN im_user im_u ON im_u._userid = im_m._from
WHERE
	im_c_m._memberId = 'lisi'--我的id
AND im_m._type = 'chatgroup'--消息类型
AND im_m._createTime > im_c_m._lastReadTime
AND im_c_g._id = 'zhufang' --群组id
--查询我在该群组的未读取的消息



SELECT
	im_m.*, im_u.*
FROM
	im_message im_m,
	im_user im_u
WHERE
	im_u._userid = im_m._from
AND im_m._createTime < 200
AND im_m._type = 'chat' 
AND im_m._from = 'lisi' --聊天对象
AND im_m._to = 'zhangsan' --自己
GROUP BY im_m._createTime desc
LIMIT 0,20
--和对方的聊天记录
 


SELECT
	im_m.*, im_u.*
FROM
	im_message im_m,
	im_chat_member im_c_m,
	im_user im_u
WHERE
	im_m._to = im_c_m._chatGroupId
AND im_u._userid = im_c_m._memberId
AND im_m._createTime < 2000
AND im_m._to = 'zhufang'--群id
AND im_m._type = 'chatgroup'
GROUP BY
	im_m._createTime DESC
LIMIT 0,20;
--获取群组的历史信息

	