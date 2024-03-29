/* The following names are synonyms for the variables in the input message. */
#define addr		mm_in.m1_p1
#define exec_name	mm_in.m1_p1
#define exec_len	mm_in.m1_i1
#define func		mm_in.m6_f1
#define grpid		(gid_t) mm_in.m1_i1
#define namelen		mm_in.m1_i1
#define pid		mm_in.m1_i1
#define seconds		mm_in.m1_i1
#define sig		mm_in.m6_i1
#define stack_bytes	mm_in.m1_i2
#define stack_ptr	mm_in.m1_p2
#define status		mm_in.m1_i1
#define usr_id		(uid_t) mm_in.m1_i1
#define request		mm_in.m2_i2
#define taddr		mm_in.m2_l1
#define data		mm_in.m2_l2
#define sig_nr		mm_in.m1_i2
#define sig_nsa		mm_in.m1_p1
#define sig_osa		mm_in.m1_p2
#define sig_ret		mm_in.m1_p3
#define sig_set		mm_in.m2_l1
#define sig_how		mm_in.m2_i1
#define sig_flags	mm_in.m2_i2
#define sig_context	mm_in.m2_p1
#ifdef _SIGMESSAGE
#define sig_msg		mm_in.m1_i1
#endif
#define reboot_flag	mm_in.m1_i1
#define reboot_code	mm_in.m1_p1
#define reboot_size	mm_in.m1_i2

/* The following names are synonyms for the variables used by profiling. */
#define profile_pid	mm_in.m1_i1
#define profile_start	mm_in.m1_i2
#define profile_end	mm_in.m1_i3
#define profile_data	mm_in.m1_p1

/* The following names are synonyms for the variables in the output message. */
#define reply_type      mm_out.m_type
#define reply_i1        mm_out.m2_i1
#define reply_p1        mm_out.m2_p1
#define ret_mask	mm_out.m2_l1 	

