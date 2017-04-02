/**
 * Copyright 2017 Tawi Commercial Services Ltd
 *
 * Licensed under the Open Software License, Version 3.0 (the “License”); you may
 * not use this file except in compliance with the License. You may obtain a copy
 * of the License at:
 * http://opensource.org/licenses/OSL-3.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */

-- These tables describe the database of BabbleSMS.
-- Refer to "HOWTO.txt" for information on how to create the database user and
-- schema.

DROP TABLE IF EXISTS status;



-- =========================
-- 1. Account Management
-- =========================
-- -------------------
-- Table status
-- -------------------
CREATE TABLE status (
    uuid VARCHAR(40) UNIQUE NOT NULL,
    description VARCHAR(70) UNIQUE NOT NULL DEFAULT 'Status Description',
    PRIMARY KEY (uuid)
) ENGINE=InnoDB;

