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

# 迁移脚本目录
migrations_dir=$rootpath/src/main/resources/flyway/migration

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

# 显示帮助信息
show_help() {
    echo -e "${CYAN}使用方法:${NC}"
    echo -e "  ${GREEN}simple.sh <action> <env> [version]${NC}"
    echo
    echo -e "${YELLOW}参数说明:${NC}"
    echo -e "  ${BLUE}action${NC} - 要执行的操作:"
    echo -e "    ${GREEN}reset${NC} - 重置数据库"
    echo -e "    ${GREEN}exec${NC} - 执行迁移"
    echo -e "    ${GREEN}dryrun${NC} - 检测脚本正确性"
    echo
    echo -e "  ${BLUE}env${NC} - 环境:"
    echo -e "    ${GREEN}local${NC} - 本地环境"
    echo -e "    ${GREEN}dev${NC} - 开发环境"
    echo -e "    ${GREEN}test${NC} - 测试环境"
    echo -e "    ${GREEN}prod${NC} - 生产环境"
    echo
    echo -e "  ${BLUE}version${NC} - 版本号 (可选，用于 exec 和 dryrun 操作)"
    echo
    echo -e "${YELLOW}示例:${NC}"
    echo -e "  ${GREEN}simple.sh reset local${NC} - 重置本地环境数据库"
    echo -e "  ${GREEN}simple.sh exec local${NC} - 更新本地环境数据库"
    echo -e "  ${GREEN}simple.sh exec local v1.0.0.1${NC} - 更新本地环境数据库到指定版本"
    echo -e "  ${GREEN}simple.sh dryrun local${NC} - 检测本地环境数据库脚本正确性"
    echo -e "  ${GREEN}simple.sh dryrun local v1.0.0.1${NC} - 检测本地环境数据库脚本到指定版本的正确性"
}

# 获取数据库URL
get_db_url() {
    local env=$1
    case $env in
        local) echo "$local" ;;
        dev) echo "$dev" ;;
        test) echo "$test" ;;
        prod) echo "$prod" ;;
        *) 
            echo -e "${RED}错误: 不支持的环境 '${env}'${NC}"
            echo -e "支持的环境: local, dev, test, prod"
            exit 1
            ;;
    esac
}

# 主函数
main() {
    # 检查参数
    if [ $# -lt 2 ] || [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
        show_help
        exit 0
    fi

    # 读取配置
    read_config

    # 获取参数
    local action=$1
    local env=$2
    local version=$3

    # 获取数据库URL
    local db_url=$(get_db_url "$env")

    # 执行操作
    case $action in
        reset)
            echo -e "${CYAN}正在重置 ${env} 环境数据库...${NC}"
            $EXECUTABLE reset --db="$db_url"
            ;;
        exec)
            echo -e "${CYAN}正在执行 ${env} 环境数据库迁移...${NC}"
            if [ -n "$version" ]; then
                $EXECUTABLE exec "$version" --db="$db_url" --dir="$migrations_dir"
            else
                $EXECUTABLE exec --db="$db_url" --dir="$migrations_dir"
            fi
            ;;
        dryrun)
            echo -e "${CYAN}正在检测 ${env} 环境数据库脚本正确性...${NC}"
            if [ -n "$version" ]; then
                $EXECUTABLE exec "$version" --db="$db_url" --dir="$migrations_dir" --dry-run
            else
                $EXECUTABLE exec --db="$db_url" --dir="$migrations_dir" --dry-run
            fi
            ;;
        *)
            echo -e "${RED}错误: 不支持的操作 '${action}'${NC}"
            echo -e "支持的操作: reset, exec, dryrun"
            exit 1
            ;;
    esac
}

main "$@" 