__author__ = 'Brandon Maier'

'''
' Serves up tasks
'''

class TaskServer:
    class Task:
        time_left = 0
        def __init__(self, computation, deadline):



    waiting_tasks = None
    period = 0
    computation = 0
    deadline = 0

    def __init__(self, waiting_tasks, period, computation, deadline):
        self.waiting_tasks = waiting_tasks
        self.period = period
        self.computation = computation
        self.deadline = deadline

    def run(self):
        waiting_tasks.push()