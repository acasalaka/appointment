```toml
name = 'PUT update appointment status'
method = 'PUT'
url = '{{url-appointment}}/appointment/update-status'
sortWeight = 6000000
id = '74fb7ea5-f347-4185-b852-cdb5b8fa3784'

[body]
type = 'JSON'
raw = '''
{
  "id": "OBG1412001",
  "status": 1,
  "nik": "1111111111111111",
  "doctorId": "OBG661",
  "date": "2024-09-23"
}'''
```
