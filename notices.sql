SELECT n.id, n.version, n.filename, v.metadata, v.content 
FROM vector_store AS v
JOIN notice AS n
ON n.id = CAST((v.metadata ->> 'notice') AS UUID);

SELECT * FROM note;

SELECT * FROM spring_ai_chat_memory;
-- DELETE FROM spring_ai_chat_memory WHERE true;