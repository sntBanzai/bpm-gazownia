UPDATE path_property SET id_path_property_value_current = NULL;

DELETE FROM path_property_value WHERE id IN (SELECT ppv.id FROM path_property_value ppv 
INNER JOIN path_property pp ON ppv.id_path_property = pp.id 
INNER JOIN path pth ON pp.id_path = pth.id INNER JOIN filesystem f ON f.id = pth.id_filesystem
WHERE f.name = 'defaultFS');

UPDATE path SET id_path_content_current = NULL;

DELETE FROM path_content WHERE id IN (SELECT pc.id FROM path_content pc 
INNER JOIN path pth ON pc.id_path = pth.id INNER JOIN filesystem f ON f.id = pth.id_filesystem
WHERE f.name = 'defaultFS');

DELETE FROM path_property WHERE id IN (SELECT pp.id 
FROM path_property pp
INNER JOIN path pth ON pp.id_path = pth.id INNER JOIN filesystem f ON f.id = pth.id_filesystem
WHERE f.name = 'defaultFS');

DELETE FROM path WHERE id IN (SELECT pth.id 
FROM path pth INNER JOIN filesystem f ON f.id = pth.id_filesystem
WHERE f.name = 'defaultFS');

DELETE FROM filesystem WHERE name = 'defaultFS';