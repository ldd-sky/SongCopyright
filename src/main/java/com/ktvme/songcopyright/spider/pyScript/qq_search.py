import requests
import sys
import urllib3
urllib3.disable_warnings()

def search_qq_music(title, current, nums):
    url = 'https://c.y.qq.com/soso/fcgi-bin/client_search_cp'
    params = {
        'w': title,
        'format': 'json',
        'p': int(current),
        'n': int(nums)
    }
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
        'Referer': 'https://y.qq.com/portal/search.html'
    }
    response = requests.get(url, params=params, headers=headers)
    if response.status_code == 200:
        return response.json()
    else:
        print(f"HTTP请求失败，状态码: {response.status_code}")
        return None


if __name__ == '__main__':
    title = sys.argv[1]
    current = sys.argv[2]
    nums = sys.argv[3]
    print(search_qq_music(title, current, nums))
