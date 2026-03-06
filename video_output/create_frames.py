#!/usr/bin/env python3
"""
牧岚视频号 - 降压指南 视频帧生成脚本
生成 9:16 竖屏图文视频帧
"""

from PIL import Image, ImageDraw, ImageFont
import os

# 视频参数
WIDTH = 1080
HEIGHT = 1920
FPS = 30
DURATION = 90  # 秒

# 输出目录
OUTPUT_DIR = "/root/.openclaw/workspace/video_output/frames"
os.makedirs(OUTPUT_DIR, exist_ok=True)

# 颜色定义
COLORS = {
    'bg_gradient_start': (70, 60, 110),  # 蓝紫渐变起点
    'bg_gradient_end': (130, 110, 180),   # 蓝紫渐变终点
    'text_primary': (255, 255, 255),      # 白色文字
    'text_secondary': (220, 220, 230),    # 浅灰色文字
    'accent_green': (100, 200, 150),      # 绿色强调
    'accent_yellow': (255, 200, 100),     # 黄色强调
    'accent_red': (255, 100, 100),        # 红色警告
    'silhouette': (30, 25, 45),           # 剪影颜色
}

def create_gradient_background(width, height, start_color, end_color):
    """创建渐变背景"""
    img = Image.new('RGB', (width, height))
    draw = ImageDraw.Draw(img)
    for y in range(height):
        ratio = y / height
        r = int(start_color[0] * (1 - ratio) + end_color[0] * ratio)
        g = int(start_color[1] * (1 - ratio) + end_color[1] * ratio)
        b = int(start_color[2] * (1 - ratio) + end_color[2] * ratio)
        draw.line([(0, y), (width, y)], fill=(r, g, b))
    return img

def draw_silhouette_person(draw, x, y, scale=1.0, pose="standing"):
    """绘制人物剪影"""
    color = COLORS['silhouette']
    base_x = x
    base_y = y
    
    # 简化的人物剪影（头部 + 身体 + 四肢）
    head_radius = int(40 * scale)
    body_width = int(60 * scale)
    body_height = int(120 * scale)
    
    # 头部
    draw.ellipse(
        [base_x - head_radius, base_y - head_radius * 2,
         base_x + head_radius, base_y],
        fill=color
    )
    
    # 身体
    draw.rectangle(
        [base_x - body_width // 2, base_y,
         base_x + body_width // 2, base_y + body_height],
        fill=color
    )
    
    # 手臂（根据姿势）
    arm_length = int(70 * scale)
    if pose == "thinking":
        # 思考姿势：一只手托下巴
        draw.line(
            [base_x - body_width // 2, base_y + 20,
             base_x - body_width // 2 - arm_length, base_y + 50],
            fill=color, width=int(12 * scale)
        )
        draw.line(
            [base_x + body_width // 2, base_y + 20,
             base_x + body_width // 2 + 30, base_y - 10],
            fill=color, width=int(12 * scale)
        )
    elif pose == "measuring":
        # 测量姿势：伸出手臂
        draw.line(
            [base_x - body_width // 2, base_y + 30,
             base_x - body_width // 2 - arm_length, base_y + 30],
            fill=color, width=int(12 * scale)
        )
        draw.line(
            [base_x + body_width // 2, base_y + 30,
             base_x + body_width // 2 + arm_length, base_y + 30],
            fill=color, width=int(12 * scale)
        )
    elif pose == "pointing":
        # 指向姿势
        draw.line(
            [base_x - body_width // 2, base_y + 30,
             base_x - body_width // 2 - arm_length, base_y - 20],
            fill=color, width=int(12 * scale)
        )
        draw.line(
            [base_x + body_width // 2, base_y + 30,
             base_x + body_width // 2 + 50, base_y + 30],
            fill=color, width=int(12 * scale)
        )
    elif pose == "running":
        # 跑步姿势
        draw.line(
            [base_x - body_width // 2, base_y + 30,
             base_x - body_width // 2 - 40, base_y + 70],
            fill=color, width=int(12 * scale)
        )
        draw.line(
            [base_x + body_width // 2, base_y + 30,
             base_x + body_width // 2 + 40, base_y + 60],
            fill=color, width=int(12 * scale)
        )
    elif pose == "sleeping":
        # 睡觉姿势（侧卧简化）
        draw.ellipse(
            [base_x - 50, base_y + 80, base_x + 30, base_y + 140],
            fill=color
        )
    else:  # standing
        # 站立姿势：手臂自然下垂
        draw.line(
            [base_x - body_width // 2, base_y + 30,
             base_x - body_width // 2, base_y + 90],
            fill=color, width=int(12 * scale)
        )
        draw.line(
            [base_x + body_width // 2, base_y + 30,
             base_x + body_width // 2, base_y + 90],
            fill=color, width=int(12 * scale)
        )
    
    # 腿
    leg_length = int(100 * scale)
    draw.line(
        [base_x - body_width // 4, base_y + body_height,
         base_x - body_width // 4, base_y + body_height + leg_length],
        fill=color, width=int(14 * scale)
    )
    draw.line(
        [base_x + body_width // 4, base_y + body_height,
         base_x + body_width // 4, base_y + body_height + leg_length],
        fill=color, width=int(14 * scale)
    )

def try_load_font(size):
    """尝试加载字体"""
    font_paths = [
        "/usr/share/fonts/noto-cjk/NotoSansCJK-Regular.ttc",
        "/usr/share/fonts/noto/NotoSansCJK-Regular.ttc",
        "/usr/share/fonts/wqy/wqy-zenhei.ttc",
        "/usr/share/fonts/dejavu/DejaVuSans.ttf",
    ]
    for path in font_paths:
        if os.path.exists(path):
            try:
                return ImageFont.truetype(path, size)
            except:
                pass
    return ImageFont.load_default()

def create_frame_01():
    """开场：血压多少才算高？"""
    img = create_gradient_background(WIDTH, HEIGHT, 
                                      COLORS['bg_gradient_start'],
                                      COLORS['bg_gradient_end'])
    draw = ImageDraw.Draw(img)
    font_large = try_load_font(72)
    font_medium = try_load_font(48)
    
    # 人物剪影（思考姿势）
    draw_silhouette_person(draw, WIDTH // 2, HEIGHT // 2 + 200, scale=1.5, pose="thinking")
    
    # 文字
    text1 = "血压多少才算高？"
    text2 = "很多人量了一辈子，其实没看懂"
    
    # 计算文字位置（居中）
    bbox1 = draw.textbbox((0, 0), text1, font=font_large)
    text1_width = bbox1[2] - bbox1[0]
    draw.text(((WIDTH - text1_width) // 2, 300), text1, fill=COLORS['text_primary'], font=font_large)
    
    bbox2 = draw.textbbox((0, 0), text2, font=font_medium)
    text2_width = bbox2[2] - bbox2[0]
    draw.text(((WIDTH - text2_width) // 2, 420), text2, fill=COLORS['text_secondary'], font=font_medium)
    
    return img

def create_frame_02():
    """血压计特写"""
    img = create_gradient_background(WIDTH, HEIGHT,
                                      COLORS['bg_gradient_start'],
                                      COLORS['bg_gradient_end'])
    draw = ImageDraw.Draw(img)
    font_large = try_load_font(64)
    
    # 人物剪影（测量姿势）
    draw_silhouette_person(draw, WIDTH // 2, HEIGHT // 2 + 300, scale=1.3, pose="measuring")
    
    # 血压计图形（简化）
    meter_x = WIDTH // 2
    meter_y = 400
    # 血压计外框
    draw.rounded_rectangle([meter_x - 200, meter_y - 100, meter_x + 200, meter_y + 150],
                          radius=20, fill=(40, 35, 55), outline=COLORS['accent_green'], width=4)
    # 显示屏
    draw.rectangle([meter_x - 180, meter_y - 80, meter_x + 180, meter_y + 20],
                  fill=(20, 18, 30))
    # 数字
    draw.text((meter_x - 100, meter_y - 60), "120/80", fill=COLORS['accent_green'], font=try_load_font(80))
    draw.text((meter_x - 80, meter_y + 40), "mmHg", fill=COLORS['text_secondary'], font=try_load_font(36))
    
    # 文字
    text = "今天用 90 秒，让你彻底明白"
    bbox = draw.textbbox((0, 0), text, font=font_large)
    text_width = bbox[2] - bbox[0]
    draw.text(((WIDTH - text_width) // 2, HEIGHT - 300), text, fill=COLORS['text_primary'], font=font_large)
    
    return img

def create_frame_03():
    """三色分区图表"""
    img = create_gradient_background(WIDTH, HEIGHT,
                                      COLORS['bg_gradient_start'],
                                      COLORS['bg_gradient_end'])
    draw = ImageDraw.Draw(img)
    font_title = try_load_font(56)
    font_label = try_load_font(40)
    
    # 标题
    title = "血压标准对照"
    bbox = draw.textbbox((0, 0), title, font=font_title)
    draw.text(((WIDTH - bbox[2] + bbox[0]) // 2, 150), title, fill=COLORS['text_primary'], font=font_title)
    
    # 人物剪影（指向姿势）
    draw_silhouette_person(draw, WIDTH // 2, HEIGHT - 300, scale=1.2, pose="pointing")
    
    # 三个区域
    bar_width = 280
    bar_height = 180
    gap = 40
    start_x = (WIDTH - (bar_width * 3 + gap * 2)) // 2
    start_y = 400
    
    # 绿色区域（正常）
    draw.rounded_rectangle([start_x, start_y, start_x + bar_width, start_y + bar_height],
                          radius=15, fill=COLORS['accent_green'])
    draw.text((start_x + bar_width // 2 - 60, start_y + 60), "正常", fill=(255, 255, 255), font=font_label)
    draw.text((start_x + bar_width // 2 - 80, start_y + 120), "<120/<80", fill=(255, 255, 255), font=try_load_font(32))
    
    # 黄色区域（前期）
    draw.rounded_rectangle([start_x + bar_width + gap, start_y, start_x + bar_width * 2 + gap, start_y + bar_height],
                          radius=15, fill=COLORS['accent_yellow'])
    draw.text((start_x + bar_width + gap + bar_width // 2 - 80, start_y + 60), "前期", fill=(50, 40, 20), font=font_label)
    draw.text((start_x + bar_width + gap + bar_width // 2 - 90, start_y + 120), "120-139", fill=(50, 40, 20), font=try_load_font(32))
    
    # 红色区域（高血压）
    draw.rounded_rectangle([start_x + (bar_width + gap) * 2, start_y, start_x + bar_width * 3 + gap * 2, start_y + bar_height],
                          radius=15, fill=COLORS['accent_red'])
    draw.text((start_x + (bar_width + gap) * 2 + bar_width // 2 - 80, start_y + 60), "高血压", fill=(255, 255, 255), font=font_label)
    draw.text((start_x + (bar_width + gap) * 2 + bar_width // 2 - 70, start_y + 120), "≥140/≥90", fill=(255, 255, 255), font=try_load_font(32))
    
    return img

def create_frame_04():
    """口诀记忆"""
    img = create_gradient_background(WIDTH, HEIGHT,
                                      COLORS['bg_gradient_start'],
                                      COLORS['bg_gradient_end'])
    draw = ImageDraw.Draw(img)
    font_huge = try_load_font(100)
    font_medium = try_load_font(48)
    
    # 人物剪影（记住手势）
    draw_silhouette_person(draw, WIDTH // 2, HEIGHT - 250, scale=1.3, pose="standing")
    
    # 口诀文字
    text1 = "记住这个口诀"
    bbox1 = draw.textbbox((0, 0), text1, font=font_medium)
    draw.text(((WIDTH - bbox1[2] + bbox1[0]) // 2, 200), text1, fill=COLORS['text_secondary'], font=font_medium)
    
    # 大字号口诀
    text2 = "12 8 正常"
    bbox2 = draw.textbbox((0, 0), text2, font=font_huge)
    draw.text(((WIDTH - bbox2[2] + bbox2[0]) // 2, 400), text2, fill=COLORS['accent_green'], font=font_huge)
    
    text3 = "14 9 警戒"
    bbox3 = draw.textbbox((0, 0), text3, font=font_huge)
    draw.text(((WIDTH - bbox3[2] + bbox3[0]) // 2, 550), text3, fill=COLORS['accent_red'], font=font_huge)
    
    return img

def create_frame_05():
    """少吃盐"""
    img = create_gradient_background(WIDTH, HEIGHT,
                                      COLORS['bg_gradient_start'],
                                      COLORS['bg_gradient_end'])
    draw = ImageDraw.Draw(img)
    font_title = try_load_font(64)
    font_content = try_load_font(48)
    
    # 人物剪影（摇头拒绝）
    draw_silhouette_person(draw, WIDTH // 2 + 200, HEIGHT // 2 + 200, scale=1.3, pose="standing")
    
    # 盐罐图标（简化）
    salt_x = 300
    salt_y = 500
    draw.rounded_rectangle([salt_x - 60, salt_y - 100, salt_x + 60, salt_y + 80],
                          radius=10, fill=(240, 240, 250))
    draw.ellipse([salt_x - 60, salt_y - 120, salt_x + 60, salt_y - 80], fill=(240, 240, 250))
    draw.text((salt_x - 40, salt_y - 30), "盐", fill=(100, 100, 120), font=try_load_font(48))
    
    # 拒绝符号
    draw.ellipse([salt_x - 100, salt_y - 140, salt_x + 100, salt_y + 100],
                outline=COLORS['accent_red'], width=8)
    draw.line([salt_x - 70, salt_y - 70, salt_x + 70, salt_y + 70],
             fill=COLORS['accent_red'], width=8)
    
    # 文字
    text1 = "一、少吃盐"
    draw.text((100, 200), text1, fill=COLORS['text_primary'], font=font_title)
    
    text2 = "每天不超过 5 克"
    draw.text((100, 300), text2, fill=COLORS['accent_yellow'], font=font_content)
    
    return img

def create_frame_06():
    """多运动"""
    img = create_gradient_background(WIDTH, HEIGHT,
                                      COLORS['bg_gradient_start'],
                                      COLORS['bg_gradient_end'])
    draw = ImageDraw.Draw(img)
    font_title = try_load_font(64)
    font_content = try_load_font(48)
    
    # 人物剪影（跑步）
    draw_silhouette_person(draw, WIDTH // 2, HEIGHT // 2 + 150, scale=1.4, pose="running")
    
    # 文字
    text1 = "二、多运动"
    draw.text((100, 200), text1, fill=COLORS['text_primary'], font=font_title)
    
    text2 = "每周 150 分钟中等强度"
    draw.text((100, 300), text2, fill=COLORS['accent_green'], font=font_content)
    
    # 运动图标
    draw.ellipse([WIDTH - 200, 200, WIDTH - 120, 280], fill=COLORS['accent_green'])
    
    return img

def create_frame_07():
    """控体重 + 戒烟限酒 + 规律作息"""
    img = create_gradient_background(WIDTH, HEIGHT,
                                      COLORS['bg_gradient_start'],
                                      COLORS['bg_gradient_end'])
    draw = ImageDraw.Draw(img)
    font_title = try_load_font(52)
    font_content = try_load_font(40)
    
    # 标题
    title = "三、四、五"
    bbox = draw.textbbox((0, 0), title, font=font_title)
    draw.text(((WIDTH - bbox[2] + bbox[0]) // 2, 150), title, fill=COLORS['text_primary'], font=font_title)
    
    # 三个要点
    points = [
        ("控体重", "BMI 18.5-24", COLORS['accent_yellow']),
        ("戒烟限酒", "保护血管", COLORS['accent_red']),
        ("规律作息", "睡眠充足", COLORS['accent_green']),
    ]
    
    for i, (title, content, color) in enumerate(points):
        y = 350 + i * 200
        # 图标背景
        draw.rounded_rectangle([100, y - 60, 250, y + 60], radius=30, fill=color)
        draw.text((175, y - 30), title, fill=(255, 255, 255), font=try_load_font(36))
        draw.text((280, y - 20), content, fill=COLORS['text_primary'], font=font_content)
    
    # 人物剪影（睡觉）
    draw_silhouette_person(draw, WIDTH - 300, HEIGHT - 300, scale=1.2, pose="sleeping")
    
    return img

def create_frame_08():
    """结尾金句"""
    img = create_gradient_background(WIDTH, HEIGHT,
                                      COLORS['bg_gradient_start'],
                                      COLORS['bg_gradient_end'])
    draw = ImageDraw.Draw(img)
    font_quote = try_load_font(56)
    font_brand = try_load_font(48)
    
    # 人物剪影（微笑挥手）
    draw_silhouette_person(draw, WIDTH // 2, HEIGHT // 2 + 200, scale=1.4, pose="standing")
    
    # 金句
    quote1 = "血压不是数字游戏"
    quote2 = "是生活方式的反馈"
    
    bbox1 = draw.textbbox((0, 0), quote1, font=font_quote)
    draw.text(((WIDTH - bbox1[2] + bbox1[0]) // 2, 400), quote1, fill=COLORS['text_primary'], font=font_quote)
    
    bbox2 = draw.textbbox((0, 0), quote2, font=font_quote)
    draw.text(((WIDTH - bbox2[2] + bbox2[0]) // 2, 500), quote2, fill=COLORS['text_secondary'], font=font_quote)
    
    # 温馨提示
    tip = "稳住血压，从生活小细节开始"
    bbox_tip = draw.textbbox((0, 0), tip, font=font_brand)
    draw.text(((WIDTH - bbox_tip[2] + bbox_tip[0]) // 2, HEIGHT - 400), tip, fill=COLORS['accent_green'], font=font_brand)
    
    return img

def create_frame_09():
    """品牌收尾"""
    img = create_gradient_background(WIDTH, HEIGHT,
                                      COLORS['bg_gradient_start'],
                                      COLORS['bg_gradient_end'])
    draw = ImageDraw.Draw(img)
    font_brand = try_load_font(72)
    font_slogan = try_load_font(48)
    
    # 人物剪影（鞠躬）
    draw_silhouette_person(draw, WIDTH // 2, HEIGHT // 2 + 100, scale=1.5, pose="standing")
    
    # 品牌名
    brand = "牧岚"
    bbox = draw.textbbox((0, 0), brand, font=font_brand)
    draw.text(((WIDTH - bbox[2] + bbox[0]) // 2, 500), brand, fill=COLORS['text_primary'], font=font_brand)
    
    # 口号
    slogan = "为健康发声"
    bbox_s = draw.textbbox((0, 0), slogan, font=font_slogan)
    draw.text(((WIDTH - bbox_s[2] + bbox_s[0]) // 2, 620), slogan, fill=COLORS['accent_green'], font=font_slogan)
    
    return img

# 生成所有帧
print("开始生成视频帧...")

frames = [
    ("frame_001.png", create_frame_01(), 15),   # 0:00-0:15 开场
    ("frame_002.png", create_frame_02(), 15),   # 0:15-0:30 血压计
    ("frame_003.png", create_frame_03(), 30),   # 0:30-1:00 三色图表
    ("frame_004.png", create_frame_04(), 20),   # 1:00-1:20 口诀
    ("frame_005.png", create_frame_05(), 10),   # 1:20-1:30 少吃盐
    ("frame_006.png", create_frame_06(), 10),   # 1:30-1:40 多运动
    ("frame_007.png", create_frame_07(), 15),   # 1:40-1:55 其他要点
    ("frame_008.png", create_frame_08(), 10),   # 1:55-2:05 金句
    ("frame_009.png", create_frame_09(), 5),    # 2:05-2:10 品牌收尾
]

for filename, img, duration in frames:
    filepath = os.path.join(OUTPUT_DIR, filename)
    img.save(filepath, "PNG")
    print(f"✓ 已生成 {filename} (持续{duration}秒)")

print(f"\n所有帧已保存到 {OUTPUT_DIR}")
print("下一步：使用 FFmpeg 合成视频")
