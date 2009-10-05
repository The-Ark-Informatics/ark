---------------------------------------------
-- [1] Set QTY_REMIAN to 0 by default
---------------------------------------------
    ALTER TABLE ix_biospecimen ALTER COLUMN "QTY_REMIAN" SET DEFAULT 0;

--------------------------------------------
-- [2] Run scripts to update biospecimen inventory
--------------------------------------------
    -- run script in bob:/home/postgres/clients/ccia/25July2005

--------------------------------------------
-- [3] Run scripts to update comments
--------------------------------------------
    -- run script in bob:/home/postgres/clients/ccia/22July2005

--------------------------------------------
-- [4] Update configuration to add users
--------------------------------------------
    -- org.jasig.portal.services.Authentication.defaultTemplateUserName = researcher1