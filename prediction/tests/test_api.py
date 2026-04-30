import pytest
import requests

BASE_URL = 'http://localhost:5000'

def test_predict_success():
    # 测试数据
    test_data = {
        '性别': 1,
        '年龄': 45,
        'C10': 0.1,
        'C12': 0.2,
        'C14:1': 0.3,
        'C14': 0.4,
        'C16:1': 0.5,
        'C16': 0.6,
        'C18:3': 0.7,
        'C18:2': 0.8,
        'C18:1': 0.9,
        'C18:0': 1.0,
        'C20:5': 1.1,
        'C20:4': 1.2,
        'C20:3': 1.3,
        'C20:2': 1.4,
        'C20:1': 1.5,
        'C20': 1.6,
        'C22:6': 1.7,
        'C22:5': 1.8,
        'C22:4': 1.9,
        'C22:1': 2.0,
        'C22': 2.1,
        'C24:1': 2.2,
        'C24': 2.3,
        'w3/w6': 2.4,
        'Triene /Tetraene': 2.5,
        'Total SFA': 2.6,
        'Total MUFA': 2.7,
        'Total PUFA': 2.8,
        'T w3': 2.9,
        'T w6': 3.0,
        'Total FA': 3.1
    }
    
    response = requests.post(f'{BASE_URL}/api/predict', json=test_data)
    assert response.status_code == 200
    
    result = response.json()
    assert 'probability' in result
    assert 'percentage' in result
    assert 0 <= result['probability'] <= 1
    assert 0 <= result['percentage'] <= 100

if __name__ == '__main__':
    pytest.main(['-v', 'test_api.py']) 