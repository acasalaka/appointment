```toml
name = 'POST add appointment'
method = 'POST'
url = '{{url-appointment}}/appointment/add'
sortWeight = 4000000
id = 'f739ea63-4093-4822-aaec-6e38dd92ed63'

[body]
type = 'JSON'
raw = '''
{
  "nik": "1234567890123456",
  "doctorId": "GGI916",
  "date": "2024-11-12"
}'''
```
