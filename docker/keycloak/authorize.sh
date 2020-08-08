curl -X POST 'http://localhost:8180/auth/realms/ProblemTracker/protocol/openid-connect/token' \
 --header 'Content-Type: application/x-www-form-urlencoded' \
 --data-urlencode 'grant_type=password' \
 --data-urlencode 'client_id=login-app' \
 --data-urlencode 'username=test1' \
 --data-urlencode 'password=test1'
