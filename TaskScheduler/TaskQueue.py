__author__ = 'extblm'

'''
' An interface for task queues
' e.g. an edf queue will order tasks by deadline
'''

class TaskQueue:

    def peek(self):
        pass

    def pop(self):
        pass

    def push(self, task):
        pass