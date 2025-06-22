# 升级脚本维护
1. 实现版本间数据库升级维护。

## 使用说明

1. 直接执行

```bash
# 还原数据库
./bin/migrate_<os>_<arch> reset  --db="数据库连接字符串"

# 执行升级
./bin/migrate_<os>_<arch> exec [version] --db="数据库连接字符串" --dir="升级脚本目录"

# 验证脚本
./bin/migrate_<os>_<arch> exec [version] --db="数据库连接字符串" --dir="升级脚本目录" --dry-run

# 升级到最新版本
./bin/migrate_linux_amd64 exec --db="数据库连接字符串" --dir="升级脚本目录"

# 升级到指定版本
./bin/migrate_linux_amd64 exec V1.0.0.11 --db="数据库连接字符串" --dir="升级脚本目录"

```

2. 通过脚本执行

- 编写配置文件`.env`

```bash
local=postgresql://postgres:air20220401@localhost:5432/migratedb?sslmode=disable
dev=postgresql://postgres:air20220401@localhost:5432/migratedb?sslmode=disable
test=postgresql://postgres:air20220401@192.168.10.44:5432/airedge2db_autotest?sslmode=disable
prod=postgresql://postgres:air20220401@localhost:5432/migratedb?sslmode=disable
```
- 执行脚本

```bash
# linux/macos
./scripts/migrate.sh

# windows
.\scripts\migrate.bat
```