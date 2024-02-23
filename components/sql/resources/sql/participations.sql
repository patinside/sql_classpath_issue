-- :name create-database
-- :command :execute
-- :result :raw
CREATE DATABASE IF NOT EXISTS participations
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

-- :name grant-user
-- :command :execute
-- :result :raw
-- :doc Allow the user to insert/etc in the new database
GRANT CREATE, ALTER, DROP, INSERT, UPDATE, DELETE, SELECT, INDEX, EXECUTE
ON participations.*
TO :i:user@'%';

-- :name create-participations-table
-- :command :execute
-- :result :raw
-- :doc Create participations table
CREATE TABLE IF NOT EXISTS participations (
  id varchar(500) NOT NULL,
  wrapper_id varchar(50) NOT NULL,
  content_account varchar(24) NOT NULL,
  viewer_account varchar(24) NOT NULL,
  widget_account varchar(24) NOT NULL,
  optins_entries text,
  form_entry text,
  PRIMARY KEY (id),
  KEY `wrapper_id-index` (wrapper_id)
) ENGINE=InnoDB;
