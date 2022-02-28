#  Copyright (c) 2022, Rafael Witak.
"""
Outputs SQL query to create the first 4 Weeks
of Jeff Nippard's Lower/Upper split program.
"""

import datetime
import os
from textwrap import dedent

# Don't forget to first create all exercises, workouts, and workout_instances!

EXERCISES = (3, 40, 41, 42, 28, 14, 43,
             1, 44, 45, 46, 13, 26, 47,
             2, 22, 12, 36, 14, 49, 29,
             16, 50, 23, 37, 52, 53, 54)

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
    exercise = iter(EXERCISES)
    rep = iter(REPS)
    rpe = iter(RPES)
    rest = iter(RESTS)

    for workout in range(23, 27):
        for number in range(7):
            curr_exercise = next(exercise)
            curr_rep = next(rep)
            curr_rpe = next(rpe)
            curr_rest = next(rest)

            for i in range(0, 3):
                _set = number * 3 + i
                #
                # if workout == 21 and _set in (18, 19, 20):
                #     query += dedent(f'''
                #     INSERT INTO "main"."workout_steps"
                #     ("workout_id", "number", "exercise_id",
                #     "duration_seconds", "weight", "rpe", "rest_seconds")
                #     VALUES ({workout}, {_set}, {curr_exercise},
                #     {curr_rep}, 0, {curr_rpe}, {curr_rest});''')
                #     continue

                query += dedent(f'''
                INSERT INTO "main"."workout_steps"
                ("workout_id", "number", "exercise_id",
                "reps", "weight", "rpe", "rest_seconds")
                VALUES ({workout}, {_set}, {curr_exercise},
                {curr_rep}, 0, {curr_rpe}, {curr_rest});''')

    return query


if __name__ == '__main__':
    now = datetime.datetime.now()
    filename = now.strftime('%Y%m%d%H%M%S') + "_create_wo_steps.txt"
    full_path = os.path.join("output", filename)

    with open(full_path, 'w') as file:
        file.write(_make_query())
