/* =====================================================================
   Global Frío - Usuario de la aplicación (SQL Server Express)
   ---------------------------------------------------------------------
   Crea el login globalfrio_app que usa el backend Spring Boot.
   Solo puede leer y escribir datos en globalfrio_db: no puede crear ni
   borrar tablas.

   Antes de ejecutarlo:
     - Ejecuta primero database/BD_GLOBALFRIO.sql.
     - Cambia ESCRIBE_AQUI_LA_CLAVE por una contraseña tuya (mínimo 8
       caracteres, con mayúsculas, minúsculas y números) y ponla también
       en backend/bd-local.properties. No subas la contraseña a GitHub.
   ===================================================================== */
USE master;
GO
IF NOT EXISTS (SELECT 1 FROM sys.server_principals WHERE name = N'globalfrio_app')
    CREATE LOGIN globalfrio_app WITH PASSWORD = N'ESCRIBE_AQUI_LA_CLAVE', CHECK_POLICY = ON;
GO

USE globalfrio_db;
GO
IF NOT EXISTS (SELECT 1 FROM sys.database_principals WHERE name = N'globalfrio_app')
    CREATE USER globalfrio_app FOR LOGIN globalfrio_app;
GO
ALTER ROLE db_datareader ADD MEMBER globalfrio_app;
ALTER ROLE db_datawriter ADD MEMBER globalfrio_app;
GO

PRINT 'LISTO: usuario globalfrio_app creado con permisos de lectura y escritura.';
