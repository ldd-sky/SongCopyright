import requests
import sys
import urllib3
urllib3.disable_warnings()


def get_album_details(albummid):
    url = 'https://c.y.qq.com/v8/fcg-bin/fcg_v8_album_info_cp.fcg'
    params = {
        'albummid': albummid,
        'format': 'json'
    }
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
        'Referer': 'https://y.qq.com/'
    }
    response = requests.get(url, params=params, headers=headers)
    if response.status_code == 200:
        return response.json()
    else:
        print(f"HTTP请求失败，状态码: {response.status_code}")
        return None


if __name__ == '__main__':
    albummid = sys.argv[1]

    print(get_album_details(albummid))
