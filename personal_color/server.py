import requests

class Server:
    def __init__(self, base_url):
        self.base_url = base_url

    def send_data(self, endpoint, data):
        url = f"{self.base_url}{endpoint}"
        response = requests.post(url, json=data)
        return response.text

    def receive_data(self, endpoint):
        url = f"{self.base_url}{endpoint}"
        response = requests.get(url)
        return response.text

# 사용 예시
base_url = 'http://example.com/api'
conn = Server(base_url)

# 데이터 보내기
send_endpoint = '/send'
receive_endpoint = '/receive'
send_data = {'key': 'value'}
print(conn.send_data(send_endpoint, send_data))

# 데이터 받기


