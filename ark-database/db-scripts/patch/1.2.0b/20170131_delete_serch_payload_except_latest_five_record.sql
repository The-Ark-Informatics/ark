use  reporting;
DELETE FROM search_result WHERE search_payload_id NOT IN (SELECT search_payload_id FROM (SELECT search_payload_id   FROM search_result ORDER BY search_payload_id DESC  LIMIT 5) x); 
DELETE FROM search_payload WHERE id NOT IN (SELECT id FROM (SELECT id   FROM search_payload ORDER BY id DESC  LIMIT 5) y); 
