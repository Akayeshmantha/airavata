--
--
-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements.  See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership.  The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License.  You may obtain a copy of the License at
--
--   http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied.  See the License for the
-- specific language governing permissions and limitations
-- under the License.
--

use app_catalog;

-- AIRAVATA-3327: Remove deprecated reservation fields
alter table GROUP_COMPUTE_RESOURCE_PREFERENCE drop column IF EXISTS RESERVATION;
alter table GROUP_COMPUTE_RESOURCE_PREFERENCE drop column IF EXISTS RESERVATION_START_TIME;
alter table GROUP_COMPUTE_RESOURCE_PREFERENCE drop column IF EXISTS RESERVATION_END_TIME;

-- AIRAVATA-3343: Add UserStorageQuota entry to StoragePreferences table
SET @AddUserStorageQuota = (SELECT IF(
    (SELECT COUNT(*)
        FROM INFORMATION_SCHEMA.COLUMNS
        WHERE table_name = 'STORAGE_PREFERENCE'
        AND column_name = 'USER_STORAGE_QUOTA'
    ) > 0,
    "SELECT 1",
    "ALTER TABLE STORAGE_PREFERENCE ADD USER_STORAGE_QUOTA BIGINT DEFAULT 0"
));

PREPARE stmt FROM @AddUserStorageQuota;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;