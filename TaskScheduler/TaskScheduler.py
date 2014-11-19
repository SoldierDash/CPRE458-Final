__author__ = 'extblm'

from TaskScheduler import TaskQueue, TaskServer

class TaskScheduler:
    waiting_tasks = None
    run_tasks = []
    task_servers = []

    def __init__(self):
        self.waiting_tasks = TaskQueue()

    # Add a task to scheduler
    def addTask(self, period, computation, deadline=None):
        self.task_servers += TaskServer()

    # Run for one time unit
    def run(self):
        pass