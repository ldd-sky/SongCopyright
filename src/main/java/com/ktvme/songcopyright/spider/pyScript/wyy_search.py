import browsercookie
import requests
import urllib3
import sys

urllib3.disable_warnings()  # 消除 warning   InsecureRequestWarning


def read_chrome_cookie(title):
    addr = "https://music.163.com/api/search/pc?s="+title+"&type=1"
    # Chrome提前在页面登录成功，这样才能顺利获取到有效的cookies
    chrome_cookies = browsercookie.chrome()
    headers = {
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36'
    }
    # 携带本地cookies请求接口
    response = requests.get(addr,cookies=chrome_cookies, headers=headers, verify=False)
    result = response.json()
    return result


if __name__ == '__main__':
    print(read_chrome_cookie(sys.argv[1]))