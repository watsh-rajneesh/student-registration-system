"""
Student Registration System CLI.

Install Python 3 for your OS.
Create virtual environment as: python3 -m venv venv
Activate virtual environment: source venv/bin/activate
Get the following packages:
pip install requests

Now execute the CLI script as:
venv/bin/python ./studentreg.py list-students

Do a -h for complete list of commands.

"""

import json
import sys
import argparse
import textwrap
import pprint
import datetime
import requests
from requests.auth import HTTPBasicAuth

"""
Global Constants
"""
PASSWORD = 'secret'
USER = 'watsh.rajneesh@sjsu.edu'
HTTPS = '6a00b426-1243-4d80-a059-a1973e3482fe'


def replace_value_with_definition(current_dict, key_to_find, definition):
    """
    This method is used to substitute the default values read from .json file
    by a value that the user specified on CLI.

    :param current_dict:
    :param key_to_find:
    :param definition:
    :return:
    """
    for key in current_dict.keys():
        if key == key_to_find:
            current_dict[key] = definition


def get_http_scheme():
    """
    Gets the http/https protocol scheme prefix for url.
    :return:
    """
    if HTTPS.lower() == 'true':
        return "https"
    else:
        return "http"


def pretty_print_json(response):
    """
    Pretty print the response.
    """
    print("Response Headers:%s" % (response.headers))
    print("Reason:%s" % (response.reason))
    try:
        res = json.dumps(response.json(), sort_keys=True, indent=4, separators=(',', ':'))
        print("[HTTPCode:%s]JSON:" % (response.status_code))
        print(res)
    except:
        if response is not None:
            print("[HTTPCode:%s]Text:[%s]" % (response.status_code, response.text))
        else:
            print("Noresponse.")


#################### HTTP method wrapper functions ################

def get_request(url, headers=None):
    """
    GETrequest.
    :paramurl:
    :return:
    """
    print('GET ' + str(url))
    if headers is not None:
        print('Request Headers:' + str(headers))
        r = requests.get(url, auth=HTTPBasicAuth(USER, PASSWORD), headers=headers)
    else:
        r = requests.get(url, auth=HTTPBasicAuth(USER, PASSWORD))
        pretty_print_json(r)
    return r


def put_request(headers, payload, url):
    """
    PUT request.
    :param headers:
    :param payload:
    :param url:
    :return:
    """
    print('PUT ' + str(url))
    print('RequestHeaders:' + str(headers))
    pp = pprint.PrettyPrinter(indent=2)
    pp.pprint(payload)
    r = requests.put(url, auth=HTTPBasicAuth(USER, PASSWORD), data=json.dumps(payload), headers=headers)
    pretty_print_json(r)
    return r


def post_request(url, payload=None, headers=None):
    """
    POST request.
    :param url:
    :param payload:
    :param headers:
    :return:
    """
    print('POST ' + str(url))
    if payload is None and headers is None:
        r = requests.post(url, auth=HTTPBasicAuth(USER, PASSWORD))
    else:
        print('RequestHeaders:' + str(headers))
    pp = pprint.PrettyPrinter(indent=2)
    pp.pprint(payload)
    r = requests.post(url, auth=HTTPBasicAuth(USER, PASSWORD), data=json.dumps(payload), headers=headers)
    pretty_print_json(r)
    return r


def delete_request(url):
    """
    DELETE request.
    :param url:
    :return:
    """
    print('DELETE ' + str(url))
    r = requests.delete(url, auth=HTTPBasicAuth(USER, PASSWORD))
    pretty_print_json(r)
    return r


############################### REST Client functions ############################

def list_students(hostPort):
    url = "%s://%s/api/v1.0/students" % (get_http_scheme(), hostPort)
    return get_request(url)


def list_student(hostPort, id):
    url = "%s://%s/api/v1.0/students/%s" % (get_http_scheme(), hostPort, id)
    return get_request(url)



################################ MAIN ############################################
def main():
    parser = argparse.ArgumentParser(description=textwrap.dedent('''\
            !!!Student Registration System!!!
            ----------------------------------
            Note: All commands below require basic authentication so you need to specify user and password with
            -u and -p options.

            Supported commands are:
                1. Student Commands:
                    1.1 list-students
                    1.2 list-student -i <student-id>
                    1.3 create-student -f create_student.json
                    1.4 delete-student -i <student-id>
                    1.5 update-student -i <student-id> -f update_student.json
                    1.6 query-students -q <json query string>
                2. Course Commands:
                    1.1 list-courses
                    1.2 list-course -i <course-id>
                    1.3 create-course
                    1.4 delete-course -i <course-id>
                    1.5 update-course -i <course-id> -f update_course.json
                    1.6 query-courses -q <json query string>

    '''), formatter_class=argparse.RawDescriptionHelpFormatter)
    # Required args
    parser.add_argument('command', help="Student Registration System Commands.See supported commands above.",
                        default='list-students')

    # Optional args
    parser.add_argument('-e', '--endpoint', help='REST endpoint <host:port>, default=localhost:8080',
                        default='localhost:8080')

    parser.add_argument('-v', '--version', action='version', version='%(prog)s 1.0')

    parser.add_argument('-i', '--id', help='Resource ID')
    parser.add_argument('-q', '--query', help='Search query', default='{}')
    parser.add_argument('-f', '--file', help='JSON payload file')

    parser.add_argument('-H', '--https', help='Use https, default=false', default='false')

    creds_group = parser.add_argument_group("Credentials Group")
    creds_group.add_argument('-u', '--user', help='user', default='admin')
    creds_group.add_argument('-p', '--password', help='password', default='admin')

    args = parser.parse_args()

    command = args.command
    hostPort = args.endpoint
    if args.file is not None:
        with open(args.file, 'r') as myFile:
            payload = eval(myFile.read())

    global HTTPS
    global USER
    global PASSWORD

    HTTPS = args.https
    if args.user is not None:
        USER = args.user
    if args.password is not None:
        PASSWORD = args.password

    print("Executed at:%s" % (datetime.datetime.now()))

    if command == "list-students":
        list_students(hostPort)


if __name__ == '__main__':
    main()
