package com.example.quizapp

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase: SupabaseClient = createSupabaseClient(
    supabaseUrl = "https://vqrmpufgwnbjafcbctwj.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZxcm1wdWZnd25iamFmY2JjdHdqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDMwNjcyNjQsImV4cCI6MjA1ODY0MzI2NH0.VdpQmWjjpoXWjGOT-TgzpVdUpgGyg0jFF2BVnrb1bHc"
) {
    install(Postgrest)
}