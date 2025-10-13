SELECT n.id, n.version, n.filename, v.metadata, v.content 
FROM vector_store AS v
JOIN notice AS n
ON n.id = CAST((v.metadata ->> 'notice') AS BIGINT);