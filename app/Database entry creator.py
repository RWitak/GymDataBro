from textwrap import dedent

exercises = ( 2, 12, 25, 18, 28, 29, 20,
              1, 11, 16, 17, 19, 15, 39,
              3, 22, 31, 32, 29, 14, 33,
             23, 24, 13, 34, 27, 26, 35)
exercise = iter(exercises)

reps = ( 6, 10, 12, 12, 12,  6, 12,
         5, 10, 10, 12, 12, 10, 12,
         8, 10, 15, 15, 15, 12, 20,
         8,  8, 10, 12, 15, 15,  8)
rep = iter(reps)

rpes = (7,7,8,9,9,7,7,
        7,8,7,8,8,8,8,
        7,8,8,8,9,8,8,
        8,8,7,7,8,8,9)
rpe = iter(rpes)

rests = (210, 150, 150,  90, 90, 90, 90,
         210, 150, 210, 150, 90, 90, 90,
         210, 150,  90,  90, 90, 90, 90,
         150, 150, 150, 150, 90, 90, 90)
rest = iter(rests)

query = ""

for workout in range(19, 23):
    for number in range(7):
        curr_exercise = next(exercise)
        curr_rep = next(rep)
        curr_rpe = next(rpe)
        curr_rest = next(rest)
        
        for i in range(1, 4):
            _set = number * 3 + i

            if workout == 21 and _set in (19, 20, 21):
                query += dedent(f'''
                INSERT INTO "main"."workout_steps"
                ("workout_id", "number", "exercise_id",
                "duration_seconds", "rpe", "rest_seconds")
                VALUES ({workout}, {_set}, {curr_exercise},
                {curr_rep}, {curr_rpe}, {curr_rest});''')
                continue
            
            query += dedent(f'''
            INSERT INTO "main"."workout_steps"
            ("workout_id", "number", "exercise_id",
            "reps", "rpe", "rest_seconds")
            VALUES ({workout}, {_set}, {curr_exercise},
            {curr_rep}, {curr_rpe}, {curr_rest});''')
            
print(query)
