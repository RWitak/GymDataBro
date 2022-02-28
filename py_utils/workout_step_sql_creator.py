#  Copyright (c) 2022, Rafael Witak.
"""
Outputs SQL query to create the first 4 Weeks
of Jeff Nippard's Lower/Upper split program.
"""

import datetime
import os
from itertools import cycle
from textwrap import dedent

# Change values to produce results. Lists are cycled.
# Don't forget to make sure all exercises exist first.

WEEKS = 4
WORKOUTS_PER_WEEK = 4
PROGRAM_ID = 7
WORKOUT_ID_START = 24
WORKOUT_NUMBER_START = 17

SETS_PER_EXERCISE = [3]
WORKOUT_NAMES = ["Lower Body #1",
                 "Upper Body #1",
                 "Lower Body #2",
                 "Upper Body #2"]

EXERCISES_PER_DAY = [7]

EXERCISES = (3, 40, 41, 42, 28, 14, 43,
             1, 44, 45, 46, 13, 26, 47,
             2, 22, 12, 36, 14, 48, 29,
             16, 49, 23, 37, 50, 51, 52)

REPS = (5, 12, 10, 12, 15, 8, 12,
        8, 8, 12, 12, 6, 15, 12,
        8, 8, 12, 8, 6, 6, 20,
        6, 6, 8, 8, 15, 15, 15)

RPES = (8, 8, 9, 8, 9, 8, 8,
        8, 8, 7, 8, 8, 9, 9,
        8, 8, 8, 9, 9, 8, 9,
        8, 8, 8, 9, 8, 8, 9)

RESTS = (210, 150, 150, 90, 90, 90, 90,
         210, 150, 150, 150, 90, 90, 90,
         210, 150, 150, 90, 90, 90, 90,
         210, 210, 150, 150, 90, 90, 90)


def _make_query() -> str:
    query = ""
    sets = cycle(SETS_PER_EXERCISE)
    names = cycle(WORKOUT_NAMES)
    exercise = cycle(EXERCISES)
    per_day = cycle(EXERCISES_PER_DAY)
    rep = cycle(REPS)
    rpe = cycle(RPES)
    rest = cycle(RESTS)

    # Create Workouts
    for name in WORKOUT_NAMES:
        query += (f'INSERT INTO "main"."workouts" ("program_id", "name") '
                  f'VALUES ({PROGRAM_ID}, "{name}");\n')
    query += '\n'

    # Create WorkoutInstances
    for i in range(WEEKS * WORKOUTS_PER_WEEK):
        query += (f'INSERT INTO "main"."workout_instances" '
                  f'("program_id", "workout_id", "workout_number", "active") '
                  f'VALUES ('
                  f'{PROGRAM_ID}, '
                  f'{WORKOUT_ID_START + (i % WORKOUTS_PER_WEEK)}, '
                  f'{WORKOUT_NUMBER_START + i}, '
                  f'1);\n')
    query += '\n'

    # Create WorkoutSteps
    for workout_id in range(WORKOUT_ID_START, WORKOUT_ID_START + WORKOUTS_PER_WEEK):
        curr_per_day = next(per_day)

        for number in range(curr_per_day):
            curr_sets = next(sets)
            curr_exercise = next(exercise)
            curr_rep = next(rep)
            curr_rpe = next(rpe)
            curr_rest = next(rest)

            for i in range(curr_sets):
                _set = number * curr_sets + i
                #
                # if workout_id == 21 and _set in (18, 19, 20):
                #     query += dedent(f'''
                #     INSERT INTO "main"."workout_steps"
                #     ("workout_id", "number", "exercise_id",
                #     "duration_seconds", "weight", "rpe", "rest_seconds")
                #     VALUES ({workout_id}, {_set}, {curr_exercise},
                #     {curr_rep}, 0, {curr_rpe}, {curr_rest});''')
                #     continue

                query += dedent(f'''
                INSERT INTO "main"."workout_steps"
                ("workout_id", "number", "exercise_id",
                "reps", "weight", "rpe", "rest_seconds", "active")
                VALUES ({workout_id}, {_set}, {curr_exercise},
                {curr_rep}, 0, {curr_rpe}, {curr_rest}, 1);''')
        query += "\n"

    return query


if __name__ == '__main__':
    now = datetime.datetime.now()
    filename = now.strftime('%Y%m%d%H%M%S') + "_create_wo_steps.txt"
    full_path = os.path.join("output", filename)

    with open(full_path, 'w') as file:
        file.write(_make_query())
