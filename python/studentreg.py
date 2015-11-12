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
from urllib.parse import quote

"""
Global Constants
"""
PASSWORD = 'password'
USER = 'admin@sjsu.edu'
HTTPS = 'false'


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
    Gets the http/https protocol scheme prefix for url.w
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


def put_request(payload, url, headers=None):
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

########### Student methods #############
def list_students(hostPort):
    """
    List all students.

    :param hostPort:
    :return:
    """
    url = "%s://%s/api/v1.0/students" % (get_http_scheme(), hostPort)
    return get_request(url)


def list_student(hostPort, id):
    """
    List a student by id.

    :param hostPort:
    :param id:
    :return:
    """
    url = "%s://%s/api/v1.0/students/%s" % (get_http_scheme(), hostPort, id)
    return get_request(url)

def update_student(hostPort, id, payload):
    """
    Update a student by id.
    This method is also used to enroll/unenroll student to courses.

    :param hostPort:
    :param id:
    :param payload:
    :return:
    """
    if payload is None:
        print("Please specify the json file with -f option")
        sys.exit(1)
    url = "%s://%s/api/v1.0/students/%s" % (get_http_scheme(), hostPort, id)
    return put_request(payload, url)

def create_student(hostPort, payload):
    """
    Create a new student.
    Each student is unique by his/her emailId specified in the payload.

    :param hostPort:
    :param payload:
    :return:
    """
    if payload is None:
        print("Please specify the json file with -f option")
        sys.exit(1)
    url = "%s://%s/api/v1.0/students" % (get_http_scheme(), hostPort)
    return post_request(url, payload)

def delete_student(hostPort, id):
    """
    Delete a student by id.

    :param hostPort:
    :param id:
    :return:
    """
    url = "%s://%s/api/v1.0/students/%s" % (get_http_scheme(), hostPort, id)
    return delete_request(url)


###### Course methods ##############
def create_course(hostPort, payload):
    """
    Creates a new course.
    A course is unique by its courseName in the payload.

    :param hostPort:
    :param payload:
    :return:
    """
    if payload is None:
        print("Please specify the json file with -f option")
        sys.exit(1)
    url = "%s://%s/api/v1.0/courses" % (get_http_scheme(), hostPort)
    return post_request(url, payload)

def list_courses(hostPort):
    """
    List all courses.

    :param hostPort:
    :return:
    """
    url = "%s://%s/api/v1.0/courses" % (get_http_scheme(), hostPort)
    return get_request(url)


def list_course(hostPort, id):
    """
    List a single course by id.

    :param hostPort:
    :param id:
    :return:
    """
    url = "%s://%s/api/v1.0/courses/%s" % (get_http_scheme(), hostPort, id)
    return get_request(url)

def query_courses(hostPort, query):
    """
    Query a course. The query string is based on the query syntax for mongodb.

    :param hostPort:
    :param query:
    :return:
    """
    quote1 = quote(query, safe='')
    print(quote1)
    url = "%s://%s/api/v1.0/courses/?filter=%s" % (get_http_scheme(), hostPort, quote1)
    return get_request(url)

def update_course(hostPort, id, payload):
    if payload is None:
        print("Please specify the json file with -f option")
        sys.exit(1)
    url = "%s://%s/api/v1.0/courses/%s" % (get_http_scheme(), hostPort, id)
    return put_request(payload, url)

def delete_course(hostPort, id):
    """
    Delete a course by id.

    :param hostPort:
    :param id:
    :return:
    """
    url = "%s://%s/api/v1.0/courses/%s" % (get_http_scheme(), hostPort, id)
    return delete_request(url)

################################ MAIN ############################################rsh

def main():
    """
    Main method of the student registration system client program.

    :return:
    """
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
    payload = None
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
    elif command == "list-student":
        list_student(hostPort, id=args.id)
    elif command == "create-student":
        create_student(hostPort, payload)
    elif command == "update-student":
        update_student(hostPort, id=args.id, payload=payload)
    elif command == "delete-student":
        delete_student(hostPort, id=args.id)
    elif command == "create-course":
        create_course(hostPort, payload)
    elif command == "list-courses":
        list_courses(hostPort)
    elif command == "list-course":
        list_course(hostPort, id=args.id)
    elif command == "query-courses":
        query_courses(hostPort, query=args.query)
    elif command == "update-course":
        update_course(hostPort, id=args.id, payload=payload)
    elif command == "delete-course":
        delete_course(hostPort, id=args.id)
    else:
        print("Unsupported command [", command, "]. \nPlease refer to help (-h option) for the list of supported commands.")
        sys.exit(1)


if __name__ == '__main__':
    main()
