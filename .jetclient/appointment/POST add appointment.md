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
  "nik": "4875445910916483",
  "doctorId": "c446b208-2f79-44b2-863e-6fa44580be2e",
  "date": "2024-11-27"
}'''
```
