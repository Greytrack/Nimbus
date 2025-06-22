#!/bin/bash

basepath=$(cd `dirname $0`; pwd)

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# 查找 bin 目录
find_bin_dir() {
    local current_dir=$1
    if [ -d "$current_dir/bin" ]; then
        echo "$current_dir"
    else
        local parent_dir=$(dirname "$current_dir")
        if [ "$parent_dir" = "$current_dir" ]; then
            echo -e "${RED}Error: bin directory not found.${NC}"
            exit 1
        fi
        find_bin_dir "$parent_dir"
    fi
}
# 查找根目录
rootpath=$(find_bin_dir "$basepath")

echo -e "${GREEN}根目录:${NC} $rootpath"

# 迁移脚本目录
migrations_dir=$rootpath/src/main/resources/flyway/migration

echo -e "${GREEN}迁移脚本目录:${NC} $migrations_dir"

# 配置文件
CONFIG_FILE="$basepath/.env"

# 操作系统类型及架构
OS=$(uname -s | tr '[:upper:]' '[:lower:]')
ARCH=$(uname -m)
if [ "$ARCH" = "x86_64" ]; then
    ARCH="amd64"
elif [ "$ARCH" = "aarch64" ]; then
    ARCH="arm64"
fi

# 可执行文件名称
EXECUTABLE="$rootpath/bin/migrate_${OS}_${ARCH}"

# 读取配置文件
read_config() {
    if [ -f "$CONFIG_FILE" ]; then
        source "$CONFIG_FILE"
    else
        echo -e "${RED}配置文件不存在:${NC} $CONFIG_FILE"
        exit 1
    fi
}

# 列出命令内容
list_commands() {
    while true; do
        echo -e "\n${CYAN}=== 主菜单 ===${NC}"
        echo -e "${YELLOW}请选择要执行的命令:${NC}"
        echo -e "${GREEN}1.${BLUE} 重置数据库"
        echo -e "${GREEN}2.${PURPLE} 执行迁移"
        echo -e "${RED}9.${NC} 退出"
        read -p "请输入序号: " choice
        case $choice in
            1) reset_database ;;
            2) execute_migration ;;
            9) echo -e "${YELLOW}退出程序${NC}"; exit 0 ;;
            *) echo -e "${RED}无效选择${NC}" ;;
        esac
    done
}

# 重置数据库子命令
reset_database() {
    echo -e "\n${CYAN}=== 重置数据库 ===${NC}"
    echo -e "${YELLOW}请选择环境:${NC}"
    echo -e "${GREEN}1.${BLUE} 本地(local)"
    echo -e "${GREEN}2.${BLUE} 开发(dev)"
    echo -e "${GREEN}3.${BLUE} 测试(test)"
    echo -e "${GREEN}4.${BLUE} 生产(prod)"
    echo -e "${GREEN}5.${BLUE} 自定义"
    echo -e "${RED}9.${NC} 返回上级菜单"
    read -p "请输入序号: " env_choice
    case $env_choice in
        1) db_url=$local ;;
        2) db_url=$dev ;;
        3) db_url=$test ;;
        4) db_url=$prod ;;
        5) read -p "请输入数据库链接: " db_url ;;
        9) return ;;
        *) echo -e "${RED}无效选择${NC}"; return ;;
    esac
    $EXECUTABLE reset --db="$db_url"
}

# 执行迁移子命令
execute_migration() {
    echo -e "\n${CYAN}=== 执行迁移 ===${NC}"
    echo -e "${YELLOW}请选择环境:${NC}"
    echo -e "${GREEN}1.${BLUE} 本地(local)"
    echo -e "${GREEN}2.${BLUE} 开发(dev)"
    echo -e "${GREEN}3.${BLUE} 测试(test)"
    echo -e "${GREEN}4.${BLUE} 生产(prod)"
    echo -e "${GREEN}5.${BLUE} 自定义"
    echo -e "${RED}9.${NC} 返回上级菜单"
    read -p "请输入序号: " env_choice
    case $env_choice in
        1) db_url=$local ;;
        2) db_url=$dev ;;
        3) db_url=$test ;;
        4) db_url=$prod ;;
        5) read -p "请输入数据库链接: " db_url ;;
        9) return ;;
        *) echo -e "${RED}无效选择${NC}"; return ;;
    esac
    read -p "请输入版本号(默认为空): " version
    read -p "请输入SQL脚本目录(默认为$migrations_dir): " dir
    dir=${dir:-$migrations_dir}
    read -p "是否测试数据库脚本(【y/n】默认为n): " dry_run
    dry_run=${dry_run:-n} 
    if [ "$dry_run" = "y" ]; then
        $EXECUTABLE exec $version --db="$db_url" --dir="$dir" --dry-run
    else
        $EXECUTABLE exec $version --db="$db_url" --dir="$dir"
    fi
}

# 主函数
main() {
    read_config
    list_commands
}

main 