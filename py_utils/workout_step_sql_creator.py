#  Copyright (c) 2022, Rafael Witak.
"""
Outputs SQL query to create the first 4 Weeks
of Jeff Nippard's Lower/Upper split program.
"""

import datetime
import os
from textwrap import dedent

EXERCISES = (2, 12, 25, 18, 28, 29, 20,
             1, 11, 16, 17, 19, 15, 39,
             3, 22, 31, 32, 29, 14, 33,
             23, 24, 13, 34, 27, 26, 35)

REPS = (6, 10, 12, 12, 12, 6, 12,
        5, 10, 10, 12, 12, 10, 12,
        8, 10, 15, 15, 15, 12, 20,
        8, 8, 10, 12, 15, 15, 8)

RPES = (7, 7, 8, 9, 9, 7, 7,
        7, 8, 7, 8, 8, 8, 8,
        7, 8, 8, 8, 9, 8, 8,
        8, 8, 7, 7, 8, 8, 9)

RESTS = (210, 150, 150, 90, 90, 90, 90,
         210, 150, 210, 150, 90, 90, 90,
         210, 150, 90, 90, 90, 90, 90,
         150, 150, 150, 150, 90, 90, 90)


def _make_query() -> str:
    query = ""
    exercise = iter(EXERCISES)
    rep = iter(REPS)
    rpe = iter(RPES)
    rest = iter(RESTS)

    for workout in range(19, 23):
        for number in range(7):
            curr_exercise = next(exercise)
            curr_rep = next(rep)
            curr_rpe = next(rpe)
            curr_rest = next(rest)

            for i in range(0, 3):
                _set = number * 3 + i

                if workout == 21 and _set in (18, 19, 20):
                    query += dedent(f'''
                    INSERT INTO "main"."workout_steps"
                    ("workout_id", "number", "exercise_id",
                    "duration_seconds", "weight", "rpe", "rest_seconds")
                    VALUES ({workout}, {_set}, {curr_exercise},
                    {curr_rep}, 0, {curr_rpe}, {curr_rest});''')
                    continue

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
