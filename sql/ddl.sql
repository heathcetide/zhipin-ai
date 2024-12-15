create database ai_interview_db;

use ai_interview_db;

-- 权限管理表：存储不同角色的权限信息
CREATE TABLE roles_permissions (
                                   role_permission_id INT AUTO_INCREMENT PRIMARY KEY,
                                   role ENUM('admin', 'interviewer', 'candidate', 'mid_level_user') NOT NULL,
                                   permission VARCHAR(100) NOT NULL,
                                   description VARCHAR(255)
);

-- 面试记录表：记录候选人在AI面试过程中的详细信息
CREATE TABLE interview_records (
                                   record_id INT AUTO_INCREMENT PRIMARY KEY,
                                   interview_id INT NOT NULL,
                                   question VARCHAR(255) NOT NULL,
                                   candidate_response TEXT NOT NULL,
                                   ai_evaluation_score DECIMAL(5, 2),
                                   evaluation_comments TEXT,
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                   FOREIGN KEY (interview_id) REFERENCES interviews(interview_id)
);

-- 面试结果表：存储每次面试的结果信息
CREATE TABLE interview_results (
                                   result_id INT AUTO_INCREMENT PRIMARY KEY,
                                   interview_id INT NOT NULL,
                                   ai_score DECIMAL(5, 2),
                                   interviewer_score DECIMAL(5, 2),
                                   final_decision ENUM('pass', 'fail', 'on_hold'),
                                   feedback TEXT,
                                   strengths TEXT,
                                   weaknesses TEXT,
                                   recommendations TEXT,
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                   FOREIGN KEY (interview_id) REFERENCES interviews(interview_id)
);

-- 面试表：存储面试安排的信息
CREATE TABLE interviews (
                            interview_id INT AUTO_INCREMENT PRIMARY KEY,
                            candidate_id INT NOT NULL,
                            interviewer_id INT NOT NULL,
                            scheduled_time DATETIME NOT NULL,
                            interview_status ENUM('scheduled', 'completed', 'cancelled') DEFAULT 'scheduled',
                            interview_type ENUM('AI', 'human', 'mixed') DEFAULT 'AI',
                            interview_location VARCHAR(255),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- 面试官分配表：记录面试官的分配情况，便于中间层级用户管理
CREATE TABLE interviewer_assignments (
                                         assignment_id INT AUTO_INCREMENT PRIMARY KEY,
                                         interviewer_id INT NOT NULL,
                                         department_id INT NOT NULL,
                                         assigned_by INT NOT NULL,
                                         assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         FOREIGN KEY (department_id) REFERENCES departments(department_id)
);



-- 面试通知表：记录面试相关的通知信息，便于候选人和面试官查阅
CREATE TABLE interview_notifications (
                                         notification_id INT AUTO_INCREMENT PRIMARY KEY,
                                         interview_id INT NOT NULL,
                                         user_id INT NOT NULL,
                                         notification_message TEXT NOT NULL,
                                         status ENUM('unread', 'read') DEFAULT 'unread',
                                         sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         FOREIGN KEY (interview_id) REFERENCES interviews(interview_id)
);

-- 聊天记录表：记录面试官和候选人之间的聊天记录，便于面试过程中的交流
CREATE TABLE chat_records (
                              chat_id INT AUTO_INCREMENT PRIMARY KEY,
                              interview_id INT NOT NULL,
                              sender_id INT NOT NULL,
                              receiver_id INT NOT NULL,
                              message TEXT NOT NULL,
                              sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              FOREIGN KEY (interview_id) REFERENCES interviews(interview_id)
);


-- AI 分析数据表：存储AI对候选人的面试分析数据
CREATE TABLE ai_analysis (
                             analysis_id INT AUTO_INCREMENT PRIMARY KEY,
                             interview_id INT NOT NULL,
                             knowledge_score DECIMAL(5, 2),
                             language_fluency_score DECIMAL(5, 2),
                             emotional_stability_score DECIMAL(5, 2),
                             logical_thinking_score DECIMAL(5, 2),
                             overall_score DECIMAL(5, 2),
                             analysis_report TEXT,
                             improvement_suggestions TEXT,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             FOREIGN KEY (interview_id) REFERENCES interviews(interview_id)
);
-- 消息队列表：存储系统中消息队列相关的记录，例如面试通知
CREATE TABLE message_queue (
                               message_id INT AUTO_INCREMENT PRIMARY KEY,
                               message_type ENUM('email', 'sms', 'notification') NOT NULL,
                               recipient_id INT NOT NULL,
                               message_content TEXT NOT NULL,
                               status ENUM('pending', 'sent', 'failed') DEFAULT 'pending',
                               error_message VARCHAR(255),
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 缓存数据记录表：用于记录需要缓存的数据，便于优化访问
CREATE TABLE cache_records (
                               cache_id INT AUTO_INCREMENT PRIMARY KEY,
                               key_name VARCHAR(255) NOT NULL,
                               data_value TEXT NOT NULL,
                               expiration_time DATETIME NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 面试反馈表：存储面试官对候选人的个性化反馈
CREATE TABLE interview_feedback (
                                    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
                                    interview_id INT NOT NULL,
                                    interviewer_id INT NOT NULL,
                                    strengths TEXT,
                                    weaknesses TEXT,
                                    recommendations TEXT,
                                    follow_up_actions TEXT,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    FOREIGN KEY (interview_id) REFERENCES interviews(interview_id)
);

-- 面试趋势表：用于记录候选人多次面试的表现趋势
CREATE TABLE interview_trends (
                                  trend_id INT AUTO_INCREMENT PRIMARY KEY,
                                  candidate_id INT NOT NULL,
                                  trend_data TEXT NOT NULL,  -- 可以存储JSON格式的数据，表示候选人的多次面试表现
                                  trend_summary TEXT,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

);

-- 部门信息表：存储中间层级用户（如面试组长、部门负责人）相关的部门信息
CREATE TABLE departments (
                             department_id INT AUTO_INCREMENT PRIMARY KEY,
                             department_name VARCHAR(100) NOT NULL,
                             department_description TEXT,
                             manager_id INT,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


-- 系统日志表：记录系统中的关键操作日志，便于系统管理员审查和维护
CREATE TABLE system_logs (
                             log_id INT AUTO_INCREMENT PRIMARY KEY,
                             user_id INT,
                             action VARCHAR(255) NOT NULL,
                             action_details TEXT,
                             action_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             ip_address VARCHAR(50),
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



create table admin
(
    id            bigint auto_increment comment '管理员ID'
        primary key,
    adminAccount  varchar(256)                          not null comment '管理员账号',
    adminPassword varchar(512)                          not null comment '管理员密码',
    salt          varchar(64) default 'cetide'          not null comment '密码盐',
    adminName     varchar(256)                          not null comment '管理员姓名',
    email         varchar(256)                          null comment '管理员邮箱',
    phone         varchar(20)                           null comment '管理员电话',
    userAvatar    varchar(1024)                         null comment '管理员头像',
    role          varchar(256)                          not null comment '角色: super_admin/admin',
    status        tinyint     default 1                 not null comment '状态: 1-正常, 0-禁用',
    createTime    datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    lastLoginTime datetime                              null comment '最后登录时间',
    lastLoginIP   varchar(45)                           null comment '最后登录IP',
    isDeleted     tinyint     default 0                 not null comment '是否删除',
    constraint adminAccount
        unique (adminAccount)
)
    comment '管理员用户表' collate = utf8mb4_unicode_ci;

create table daily_question
(
    id           bigint auto_increment comment '记录 id'
        primary key,
    question_id  bigint       not null comment '随机选中的题目 id',
    date         date         not null comment '题目对应的日期',
    question_img varchar(255) null,
    constraint idx_date
        unique (date)
)
    comment '每日题目记录' collate = utf8mb4_unicode_ci;

create table post
(
    id         bigint auto_increment comment 'id'
        primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '帖子' collate = utf8mb4_unicode_ci;

create index idx_userId
    on post (userId);

create table post_favour
(
    id         bigint auto_increment comment 'id'
        primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '帖子收藏';

create index idx_postId
    on post_favour (postId);

create index idx_userId
    on post_favour (userId);

create table post_thumb
(
    id         bigint auto_increment comment 'id'
        primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '帖子点赞';

create index idx_postId
    on post_thumb (postId);

create index idx_userId
    on post_thumb (userId);

create table question
(
    id          bigint auto_increment comment 'id'
        primary key,
    title       varchar(512)                       null comment '标题',
    content     text                               null comment '内容',
    tags        varchar(1024)                      null comment '标签列表（json 数组）',
    answer      text                               null comment '题目答案',
    submitNum   int      default 0                 not null comment '题目提交数',
    acceptedNum int      default 0                 not null comment '题目通过数',
    judgeCase   text                               null comment '判题用例（json 数组）',
    judgeConfig text                               null comment '判题配置（json 对象）',
    thumbNum    int      default 0                 not null comment '点赞数',
    favourNum   int      default 0                 not null comment '收藏数',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '题目' collate = utf8mb4_unicode_ci;

create index idx_userId
    on question (userId);

create table question_set
(
    id          bigint auto_increment comment 'id'
        primary key,
    name        varchar(255)                       not null comment '题目集名称',
    description text                               null comment '题目集描述',
    questionIds text                               not null comment '题目 id 列表 (json 数组)',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    userId      bigint                             not null comment '创建用户 id',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '题目集' collate = utf8mb4_unicode_ci;

create index idx_userId
    on question_set (userId);

create table question_submit
(
    id         bigint auto_increment comment 'id'
        primary key,
    language   varchar(128)                       not null comment '编程语言',
    code       text                               not null comment '用户代码',
    judgeInfo  text                               null comment '判题信息（json 对象）',
    status     int      default 0                 not null comment '判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '题目提交';

create index idx_questionId
    on question_submit (questionId);

create index idx_userId
    on question_submit (userId);

create table user
(
    id                  bigint auto_increment comment '用户ID'
        primary key,
    userAccount         varchar(256)                           not null comment '账号',
    userPassword        varchar(512)                           not null comment '密码（哈希）',
    salt                varchar(64)  default 'cetide'          not null comment '密码盐',
    unionId             varchar(256)                           null comment '微信开放平台ID',
    mpOpenId            varchar(256)                           null comment '公众号OpenID',
    userName            varchar(256)                           null comment '用户昵称',
    userAvatar          varchar(1024)                          null comment '用户头像',
    userProfile         varchar(512)                           null comment '用户简介',
    userRole            varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime          datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime          datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete            tinyint      default 0                 not null comment '是否删除',
    email               varchar(256)                           null comment '用户邮箱',
    phone               varchar(20)                            null comment '用户手机号',
    isVerified          tinyint      default 0                 not null comment '邮箱/手机号是否验证',
    lastLogin           datetime                               null comment '最后登录时间',
    failedLoginAttempts int          default 0                 null comment '连续失败登录次数',
    accountLocked       tinyint      default 0                 null comment '账户是否被锁定',
    privacySettings     json                                   null comment '用户隐私设置',
    preferences         json                                   null comment '用户偏好设置',
    twoFactorEnabled    tinyint      default 0                 null comment '是否启用双因素认证',
    lastPasswordChange  datetime                               null comment '最后密码更改时间',
    gender              varchar(2)                             null,
    backgroundImg       varchar(255)                           null,
    score               mediumtext                             null
)
    comment '用户表' collate = utf8mb4_unicode_ci;

create index idx_email
    on user (email);

create index idx_phone
    on user (phone);

create index idx_unionId
    on user (unionId);

