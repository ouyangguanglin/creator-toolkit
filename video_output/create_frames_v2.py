#!/usr/bin/env python3
"""
牧岚视频号 - 降压指南 视频帧生成脚本（卡通可爱版）
全程中文，圆润可爱风格
"""

from PIL import Image, ImageDraw, ImageFont
import os
import math

# 视频参数
WIDTH = 1080
HEIGHT = 1920
FPS = 30
DURATION = 90  # 秒

# 输出目录
OUTPUT_DIR = "/root/.openclaw/workspace/video_output/frames_v2"
os.makedirs(OUTPUT_DIR, exist_ok=True)

# 可爱配色方案（暖色调）
COLORS = {
    'bg_top': (255, 230, 240),      # 粉色渐变顶部
    'bg_bottom': (255, 245, 220),   # 暖黄渐变底部
    'text_primary': (102, 51, 51),  # 深棕色文字
    'text_secondary': (153, 102, 102),  # 浅棕色文字
    'accent_pink': (255, 150, 180),  # 粉色强调
    'accent_orange': (255, 180, 120),  # 橙色强调
    'accent_green': (150, 220, 150),  # 绿色强调
    'accent_blue': (150, 200, 255),  # 蓝色强调
    'accent_yellow': (255, 220, 120),  # 黄色强调
    'accent_red': (255, 120, 120),  # 红色强调
    'outline': (180, 130, 130),     # 轮廓线
    'white': (255, 255, 255),
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

def draw_rounded_rectangle(draw, coords, radius, fill, outline=None, width=0):
    """绘制圆角矩形"""
    x1, y1, x2, y2 = coords
    draw.rounded_rectangle([x1, y1, x2, y2], radius=radius, fill=fill, outline=outline, width=width)

def draw_cute_character(draw, x, y, scale=1.0, pose="happy", expression="smile"):
    """绘制可爱卡通人物（Q 版圆润风格）"""
    # 使用暖色调绘制可爱角色
    skin_color = (255, 220, 180)  # 肤色
    hair_color = (102, 51, 40)    # 棕色头发
    clothes_color = (255, 180, 150)  # 粉色衣服
    outline = COLORS['outline']
    
    base_x = x
    base_y = y
    
    # 头部（圆形）
    head_radius = int(70 * scale)
    draw.ellipse([base_x - head_radius, base_y - head_radius, 
                  base_x + head_radius, base_y + head_radius], 
                 fill=skin_color, outline=outline, width=3)
    
    # 头发（圆形顶部）
    draw.arc([base_x - head_radius - 5, base_y - head_radius - 20,
              base_x + head_radius + 5, base_y + head_radius - 20],
             start=180, end=360, fill=hair_color, width=25)
    
    # 眼睛（大而圆）
    eye_radius = int(12 * scale)
    eye_y = base_y - 15
    # 左眼
    draw.ellipse([base_x - 35 - eye_radius, eye_y - eye_radius,
                  base_x - 35 + eye_radius, eye_y + eye_radius],
                 fill=(50, 30, 20), outline=outline, width=2)
    # 右眼
    draw.ellipse([base_x + 35 - eye_radius, eye_y - eye_radius,
                  base_x + 35 + eye_radius, eye_y + eye_radius],
                 fill=(50, 30, 20), outline=outline, width=2)
    
    # 眼睛高光
    highlight_radius = int(4 * scale)
    draw.ellipse([base_x - 35 - eye_radius + 3, eye_y - eye_radius + 3,
                  base_x - 35 - eye_radius + 3 + highlight_radius * 2, eye_y - eye_radius + 3 + highlight_radius * 2],
                 fill=(255, 255, 255))
    draw.ellipse([base_x + 35 - eye_radius + 3, eye_y - eye_radius + 3,
                  base_x + 35 - eye_radius + 3 + highlight_radius * 2, eye_y - eye_radius + 3 + highlight_radius * 2],
                 fill=(255, 255, 255))
    
    # 腮红（圆形）
    blush_radius = int(10 * scale)
    draw.ellipse([base_x - 55 - blush_radius, base_y + 5 - blush_radius,
                  base_x - 55 + blush_radius, base_y + 5 + blush_radius],
                 fill=(255, 180, 180, 180))
    draw.ellipse([base_x + 55 - blush_radius, base_y + 5 - blush_radius,
                  base_x + 55 + blush_radius, base_y + 5 + blush_radius],
                 fill=(255, 180, 180, 180))
    
    # 嘴巴（微笑弧线）
    if expression == "smile":
        draw.arc([base_x - 20, base_y + 15, base_x + 20, base_y + 35],
                 start=30, end=150, fill=(150, 80, 80), width=3)
    
    # 身体（圆润的椭圆形）
    body_width = int(80 * scale)
    body_height = int(100 * scale)
    draw.ellipse([base_x - body_width, base_y + head_radius - 10,
                  base_x + body_width, base_y + head_radius + body_height - 10],
                 fill=clothes_color, outline=outline, width=3)
    
    # 衣服装饰（小爱心）
    heart_x = base_x
    heart_y = base_y + head_radius + body_height // 2 - 20
    draw.polygon([(heart_x - 8, heart_y - 5), (heart_x, heart_y - 12), 
                  (heart_x + 8, heart_y - 5), (heart_x, heart_y + 8)],
                 fill=(255, 100, 100))
    
    # 手臂（圆润的线条）
    arm_width = int(15 * scale)
    if pose == "thinking":
        # 思考姿势：一只手托下巴
        draw.line([(base_x - body_width + 10, base_y + head_radius + 20),
                   (base_x - 30, base_y + head_radius - 20)],
                  fill=clothes_color, width=arm_width)
        draw.line([(base_x + body_width - 10, base_y + head_radius + 20),
                   (base_x + 50, base_y + head_radius + 50)],
                  fill=clothes_color, width=arm_width)
    elif pose == "pointing":
        # 指向姿势
        draw.line([(base_x - body_width + 10, base_y + head_radius + 20),
                   (base_x - 80, base_y + head_radius)],
                  fill=clothes_color, width=arm_width)
        draw.line([(base_x + body_width - 10, base_y + head_radius + 20),
                   (base_x + 80, base_y + head_radius)],
                  fill=clothes_color, width=arm_width)
    elif pose == "measuring":
        # 测量姿势：伸出手臂
        draw.line([(base_x - body_width + 10, base_y + head_radius + 20),
                   (base_x - 100, base_y + head_radius + 20)],
                  fill=clothes_color, width=arm_width)
        draw.line([(base_x + body_width - 10, base_y + head_radius + 20),
                   (base_x + 100, base_y + head_radius + 20)],
                  fill=clothes_color, width=arm_width)
    elif pose == "exercise":
        # 运动姿势
        draw.line([(base_x - body_width + 10, base_y + head_radius + 20),
                   (base_x - 60, base_y + head_radius - 20)],
                  fill=clothes_color, width=arm_width)
        draw.line([(base_x + body_width - 10, base_y + head_radius + 20),
                   (base_x + 60, base_y + head_radius - 20)],
                  fill=clothes_color, width=arm_width)
    else:  # happy/standing
        # 自然站立
        draw.line([(base_x - body_width + 10, base_y + head_radius + 20),
                   (base_x - body_width - 20, base_y + head_radius + 60)],
                  fill=clothes_color, width=arm_width)
        draw.line([(base_x + body_width - 10, base_y + head_radius + 20),
                   (base_x + body_width + 20, base_y + head_radius + 60)],
                  fill=clothes_color, width=arm_width)
    
    # 腿（短而圆）
    leg_width = int(20 * scale)
    draw.line([(base_x - 25, base_y + head_radius + body_height - 15),
               (base_x - 25, base_y + head_radius + body_height + 30)],
              fill=(80, 50, 40), width=leg_width)
    draw.line([(base_x + 25, base_y + head_radius + body_height - 15),
               (base_x + 25, base_y + head_radius + body_height + 30)],
              fill=(80, 50, 40), width=leg_width)
    
    # 鞋子（圆形）
    draw.ellipse([base_x - 35, base_y + head_radius + body_height + 25,
                  base_x - 15, base_y + head_radius + body_height + 40],
                 fill=(100, 60, 50), outline=outline, width=2)
    draw.ellipse([base_x + 15, base_y + head_radius + body_height + 25,
                  base_x + 35, base_y + head_radius + body_height + 40],
                 fill=(100, 60, 50), outline=outline, width=2)

def draw_cute_icon(draw, x, y, icon_type, size=60):
    """绘制可爱图标"""
    if icon_type == "salt":
        # 盐罐（圆润的罐子）
        draw.rounded_rectangle([x - 30, y - 40, x + 30, y + 30], radius=15, 
                              fill=(240, 240, 250), outline=COLORS['outline'], width=2)
        draw.ellipse([x - 30, y - 50, x + 30, y - 30], fill=(240, 240, 250), outline=COLORS['outline'], width=2)
        draw.text((x - 15, y - 10), "盐", fill=(150, 150, 180), font=try_load_font(32))
    elif icon_type == "exercise":
        # 运动图标（小跑步人）
        draw.ellipse([x - 35, y - 35, x + 35, y + 35], fill=COLORS['accent_green'], outline=COLORS['outline'], width=2)
        # 简化的跑步小人
        draw.ellipse([x - 8, y - 20, x + 8, y - 4], fill=(100, 60, 40))  # 头
        draw.line([(x, y - 4), (x, y + 15)], fill=(80, 180, 120), width=5)  # 身体
        draw.line([(x, y + 5), (x - 15, y + 20)], fill=(80, 180, 120), width=4)  # 腿
        draw.line([(x, y + 5), (x + 15, y + 25)], fill=(80, 180, 120), width=4)
    elif icon_type == "weight":
        # 体重秤
        draw.rounded_rectangle([x - 40, y - 30, x + 40, y + 30], radius=10,
                              fill=(200, 220, 255), outline=COLORS['outline'], width=2)
        draw.ellipse([x - 15, y - 15, x + 15, y + 15], fill=(255, 255, 255), outline=COLORS['outline'], width=2)
        draw.text((x - 10, y - 8), "kg", fill=(100, 100, 150), font=try_load_font(20))
    elif icon_type == "sleep":
        # 睡眠图标（月亮 + Zzz）
        draw.arc([x - 30, y - 30, x + 30, y + 30], start=0, end=180,
                fill=COLORS['accent_blue'], width=12)
        draw.text((x + 10, y - 25), "Z", fill=(150, 180, 220), font=try_load_font(24))
        draw.text((x + 25, y - 35), "z", fill=(150, 180, 220), font=try_load_font(18))
    elif icon_type == "heart":
        # 爱心
        draw.polygon([(x - 15, y - 5), (x, y - 20), (x + 15, y - 5),
                      (x, y + 20)], fill=COLORS['accent_pink'], outline=COLORS['outline'], width=2)
    elif icon_type == "forbidden":
        # 禁止符号
        draw.ellipse([x - 35, y - 35, x + 35, y + 35], fill=(255, 200, 200), 
                    outline=COLORS['accent_red'], width=4)
        draw.line([x - 25, y - 25, x + 25, y + 25], fill=COLORS['accent_red'], width=4)

def try_load_font(size):
    """尝试加载中文字体"""
    font_paths = [
        "/usr/share/fonts/noto-cjk/NotoSansCJK-Regular.ttc",
        "/usr/share/fonts/noto-cjk/NotoSansSC-Regular.otf",
        "/usr/share/fonts/wqy/wqy-zenhei.ttc",
        "/usr/share/fonts/wqy/wqy-microhei.ttc",
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
    img = create_gradient_background(WIDTH, HEIGHT, COLORS['bg_top'], COLORS['bg_bottom'])
    draw = ImageDraw.Draw(img)
    font_large = try_load_font(72)
    font_medium = try_load_font(48)
    
    # 可爱卡通人物（思考姿势）
    draw_cute_character(draw, WIDTH // 2, HEIGHT // 2 + 250, scale=1.8, pose="thinking", expression="smile")
    
    # 装饰元素（小星星）
    for i in range(5):
        star_x = 100 + i * 200
        star_y = 150 + (i % 2) * 30
        draw.polygon([(star_x, star_y - 15), (star_x + 5, star_y - 5),
                      (star_x + 15, star_y - 5), (star_x + 7, star_y + 5),
                      (star_x + 10, star_y + 15), (star_x, star_y + 8),
                      (star_x - 10, star_y + 15), (star_x - 7, star_y + 5),
                      (star_x - 15, star_y - 5), (star_x - 5, star_y - 5)],
                     fill=COLORS['accent_yellow'])
    
    # 文字
    text1 = "血压多少才算高？"
    text2 = "很多人量了一辈子，其实没看懂"
    
    bbox1 = draw.textbbox((0, 0), text1, font=font_large)
    text1_width = bbox1[2] - bbox1[0]
    draw.text(((WIDTH - text1_width) // 2, 280), text1, fill=COLORS['text_primary'], font=font_large)
    
    bbox2 = draw.textbbox((0, 0), text2, font=font_medium)
    text2_width = bbox2[2] - bbox2[0]
    draw.text(((WIDTH - text2_width) // 2, 400), text2, fill=COLORS['text_secondary'], font=font_medium)
    
    return img

def create_frame_02():
    """血压计特写"""
    img = create_gradient_background(WIDTH, HEIGHT, COLORS['bg_top'], COLORS['bg_bottom'])
    draw = ImageDraw.Draw(img)
    font_large = try_load_font(64)
    font_medium = try_load_font(40)
    
    # 可爱卡通人物（测量姿势）
    draw_cute_character(draw, WIDTH // 2, HEIGHT - 350, scale=1.5, pose="measuring", expression="smile")
    
    # 可爱血压计（圆润风格）
    meter_x = WIDTH // 2
    meter_y = 380
    
    # 血压计外框（圆角矩形）
    draw_rounded_rectangle(draw, [meter_x - 220, meter_y - 120, meter_x + 220, meter_y + 170],
                          radius=40, fill=(255, 255, 255), outline=COLORS['accent_pink'], width=5)
    
    # 显示屏
    draw_rounded_rectangle(draw, [meter_x - 200, meter_y - 100, meter_x + 200, meter_y + 40],
                          radius=20, fill=(230, 250, 255), outline=COLORS['accent_blue'], width=3)
    
    # 数字（大号）
    draw.text((meter_x - 120, meter_y - 70), "120/80", fill=COLORS['text_primary'], font=try_load_font(80))
    draw.text((meter_x - 60, meter_y + 50), "mmHg", fill=COLORS['text_secondary'], font=try_load_font(32))
    
    # 装饰（小爱心）
    for i in range(3):
        heart_x = meter_x - 150 + i * 150
        draw.polygon([(heart_x - 8, meter_y - 140 - 5), (heart_x, meter_y - 140 - 12),
                      (heart_x + 8, meter_y - 140 - 5), (heart_x, meter_y - 140 + 8)],
                     fill=COLORS['accent_pink'])
    
    # 文字
    text = "今天用 90 秒，让你彻底明白"
    bbox = draw.textbbox((0, 0), text, font=font_large)
    text_width = bbox[2] - bbox[0]
    draw.text(((WIDTH - text_width) // 2, HEIGHT - 280), text, fill=COLORS['text_primary'], font=font_large)
    
    return img

def create_frame_03():
    """三色分区图表"""
    img = create_gradient_background(WIDTH, HEIGHT, COLORS['bg_top'], COLORS['bg_bottom'])
    draw = ImageDraw.Draw(img)
    font_title = try_load_font(56)
    font_label = try_load_font(40)
    font_number = try_load_font(36)
    
    # 标题
    title = "血压标准对照"
    bbox = draw.textbbox((0, 0), title, font=font_title)
    draw.text(((WIDTH - bbox[2] + bbox[0]) // 2, 120), title, fill=COLORS['text_primary'], font=font_title)
    
    # 可爱卡通人物（指向姿势）
    draw_cute_character(draw, WIDTH // 2, HEIGHT - 280, scale=1.4, pose="pointing", expression="smile")
    
    # 三个圆角区域
    bar_width = 280
    bar_height = 200
    gap = 30
    start_x = (WIDTH - (bar_width * 3 + gap * 2)) // 2
    start_y = 350
    
    # 绿色区域（正常）
    draw_rounded_rectangle(draw, [start_x, start_y, start_x + bar_width, start_y + bar_height],
                          radius=30, fill=COLORS['accent_green'], outline=COLORS['outline'], width=3)
    draw.text((start_x + bar_width // 2 - 60, start_y + 50), "正常", fill=(255, 255, 255), font=font_label)
    draw.text((start_x + bar_width // 2 - 80, start_y + 110), "<120/<80", fill=(255, 255, 255), font=font_number)
    # 笑脸
    draw.ellipse([start_x + bar_width // 2 - 20, start_y + 150, start_x + bar_width // 2 + 20, start_y + 190],
                fill=(255, 255, 255))
    draw.arc([start_x + bar_width // 2 - 15, start_y + 155, start_x + bar_width // 2 + 15, start_y + 185],
            start=200, end=340, fill=(100, 180, 100), width=3)
    
    # 黄色区域（前期）
    draw_rounded_rectangle(draw, [start_x + bar_width + gap, start_y, start_x + bar_width * 2 + gap, start_y + bar_height],
                          radius=30, fill=COLORS['accent_yellow'], outline=COLORS['outline'], width=3)
    draw.text((start_x + bar_width + gap + bar_width // 2 - 80, start_y + 50), "前期", fill=(80, 50, 20), font=font_label)
    draw.text((start_x + bar_width + gap + bar_width // 2 - 90, start_y + 110), "120-139", fill=(80, 50, 20), font=font_number)
    # 平脸
    draw.ellipse([start_x + bar_width + gap + bar_width // 2 - 20, start_y + 150,
                  start_x + bar_width + gap + bar_width // 2 + 20, start_y + 190], fill=(255, 255, 255))
    draw.line([start_x + bar_width + gap + bar_width // 2 - 15, start_y + 170,
               start_x + bar_width + gap + bar_width // 2 + 15, start_y + 170],
              fill=(150, 100, 50), width=3)
    
    # 红色区域（高血压）
    draw_rounded_rectangle(draw, [start_x + (bar_width + gap) * 2, start_y, start_x + bar_width * 3 + gap * 2, start_y + bar_height],
                          radius=30, fill=COLORS['accent_pink'], outline=COLORS['outline'], width=3)
    draw.text((start_x + (bar_width + gap) * 2 + bar_width // 2 - 80, start_y + 50), "高血压", fill=(255, 255, 255), font=font_label)
    draw.text((start_x + (bar_width + gap) * 2 + bar_width // 2 - 70, start_y + 110), "≥140/≥90", fill=(255, 255, 255), font=font_number)
    # 哭脸
    draw.ellipse([start_x + (bar_width + gap) * 2 + bar_width // 2 - 20, start_y + 150,
                  start_x + (bar_width + gap) * 2 + bar_width // 2 + 20, start_y + 190], fill=(255, 255, 255))
    draw.arc([start_x + (bar_width + gap) * 2 + bar_width // 2 - 15, start_y + 155,
              start_x + (bar_width + gap) * 2 + bar_width // 2 + 15, start_y + 185],
            start=200, end=340, fill=(180, 80, 80), width=3)
    
    return img

def create_frame_04():
    """口诀记忆"""
    img = create_gradient_background(WIDTH, HEIGHT, COLORS['bg_top'], COLORS['bg_bottom'])
    draw = ImageDraw.Draw(img)
    font_huge = try_load_font(100)
    font_medium = try_load_font(48)
    
    # 可爱卡通人物（开心姿势）
    draw_cute_character(draw, WIDTH // 2, HEIGHT - 300, scale=1.5, pose="happy", expression="smile")
    
    # 装饰（星星、爱心）
    for i in range(8):
        x = 100 + (i % 4) * 250
        y = 150 + (i // 4) * 100
        if i % 2 == 0:
            draw.polygon([(x, y - 15), (x + 5, y - 5), (x + 15, y - 5),
                          (x + 7, y + 5), (x + 10, y + 15), (x, y + 8),
                          (x - 10, y + 15), (x - 7, y + 5), (x - 15, y - 5), (x - 5, y - 5)],
                         fill=COLORS['accent_yellow'])
        else:
            draw.polygon([(x - 15, y - 5), (x, y - 20), (x + 15, y - 5),
                          (x, y + 20)], fill=COLORS['accent_pink'])
    
    # 口诀文字
    text1 = "记住这个口诀"
    bbox1 = draw.textbbox((0, 0), text1, font=font_medium)
    draw.text(((WIDTH - bbox1[2] + bbox1[0]) // 2, 180), text1, fill=COLORS['text_secondary'], font=font_medium)
    
    # 大字号口诀（圆角背景框）
    draw_rounded_rectangle(draw, [WIDTH // 2 - 350, 320, WIDTH // 2 + 350, 480],
                          radius=40, fill=COLORS['accent_green'], outline=COLORS['outline'], width=4)
    text2 = "128 正常"
    bbox2 = draw.textbbox((0, 0), text2, font=font_huge)
    draw.text(((WIDTH - bbox2[2] + bbox2[0]) // 2, 350), text2, fill=(255, 255, 255), font=font_huge)
    
    draw_rounded_rectangle(draw, [WIDTH // 2 - 350, 520, WIDTH // 2 + 350, 680],
                          radius=40, fill=COLORS['accent_pink'], outline=COLORS['outline'], width=4)
    text3 = "149 警戒"
    bbox3 = draw.textbbox((0, 0), text3, font=font_huge)
    draw.text(((WIDTH - bbox3[2] + bbox3[0]) // 2, 550), text3, fill=(255, 255, 255), font=font_huge)
    
    return img

def create_frame_05():
    """少吃盐"""
    img = create_gradient_background(WIDTH, HEIGHT, COLORS['bg_top'], COLORS['bg_bottom'])
    draw = ImageDraw.Draw(img)
    font_title = try_load_font(64)
    font_content = try_load_font(48)
    
    # 可爱卡通人物（摇头拒绝）
    draw_cute_character(draw, WIDTH - 350, HEIGHT // 2 + 150, scale=1.4, pose="happy", expression="smile")
    
    # 盐罐图标（带禁止符号）
    draw_cute_icon(draw, 300, 500, "salt", size=80)
    draw_cute_icon(draw, 300, 500, "forbidden", size=80)
    
    # 文字
    text1 = "一、少吃盐"
    draw.text((100, 250), text1, fill=COLORS['text_primary'], font=font_title)
    
    text2 = "每天不超过 5 克"
    draw.text((100, 350), text2, fill=COLORS['accent_orange'], font=font_content)
    
    # 装饰（小盐粒）
    for i in range(5):
        x = 500 + i * 80
        y = 450 + (i % 2) * 30
        draw.ellipse([x - 8, y - 8, x + 8, y + 8], fill=(240, 240, 250), outline=COLORS['outline'], width=2)
    
    return img

def create_frame_06():
    """多运动"""
    img = create_gradient_background(WIDTH, HEIGHT, COLORS['bg_top'], COLORS['bg_bottom'])
    draw = ImageDraw.Draw(img)
    font_title = try_load_font(64)
    font_content = try_load_font(48)
    
    # 可爱卡通人物（运动姿势）
    draw_cute_character(draw, WIDTH // 2, HEIGHT // 2 + 100, scale=1.6, pose="exercise", expression="smile")
    
    # 文字
    text1 = "二、多运动"
    draw.text((100, 200), text1, fill=COLORS['text_primary'], font=font_title)
    
    text2 = "每周 150 分钟中等强度"
    draw.text((100, 300), text2, fill=COLORS['accent_green'], font=font_content)
    
    # 运动图标
    draw_cute_icon(draw, WIDTH - 200, 250, "exercise", size=80)
    
    # 装饰（汗珠、心跳）
    for i in range(3):
        x = WIDTH // 2 - 100 + i * 100
        y = HEIGHT - 350
        draw.ellipse([x - 5, y - 10, x + 5, y + 10], fill=(150, 200, 255), outline=COLORS['outline'], width=2)
    
    return img

def create_frame_07():
    """控体重 + 戒烟限酒 + 规律作息"""
    img = create_gradient_background(WIDTH, HEIGHT, COLORS['bg_top'], COLORS['bg_bottom'])
    draw = ImageDraw.Draw(img)
    font_title = try_load_font(52)
    font_content = try_load_font(40)
    
    # 标题
    title = "三、四、五"
    bbox = draw.textbbox((0, 0), title, font=font_title)
    draw.text(((WIDTH - bbox[2] + bbox[0]) // 2, 120), title, fill=COLORS['text_primary'], font=font_title)
    
    # 三个要点（圆角卡片）
    points = [
        ("控体重", "BMI 18.5-24", COLORS['accent_orange'], "weight"),
        ("戒烟限酒", "保护血管", COLORS['accent_pink'], "heart"),
        ("规律作息", "睡眠充足", COLORS['accent_blue'], "sleep"),
    ]
    
    for i, (title_text, content, color, icon) in enumerate(points):
        y = 250 + i * 220
        # 卡片背景
        draw_rounded_rectangle(draw, [80, y - 70, WIDTH - 80, y + 70], radius=35,
                              fill=(255, 255, 255), outline=color, width=4)
        # 图标
        draw_cute_icon(draw, 160, y, icon, size=50)
        # 文字
        draw.text((260, y - 35), title_text, fill=COLORS['text_primary'], font=font_title)
        draw.text((260, y + 15), content, fill=COLORS['text_secondary'], font=font_content)
    
    # 可爱卡通人物（睡觉）
    draw_cute_character(draw, WIDTH - 280, HEIGHT - 250, scale=1.3, pose="happy", expression="smile")
    
    return img

def create_frame_08():
    """结尾金句"""
    img = create_gradient_background(WIDTH, HEIGHT, COLORS['bg_top'], COLORS['bg_bottom'])
    draw = ImageDraw.Draw(img)
    font_quote = try_load_font(56)
    font_brand = try_load_font(48)
    
    # 可爱卡通人物（开心挥手）
    draw_cute_character(draw, WIDTH // 2, HEIGHT // 2 + 150, scale=1.6, pose="happy", expression="smile")
    
    # 装饰（爱心、星星）
    for i in range(10):
        x = 100 + (i % 5) * 200
        y = 150 + (i // 5) * 100
        if i % 3 == 0:
            draw.polygon([(x - 15, y - 5), (x, y - 20), (x + 15, y - 5),
                          (x, y + 20)], fill=COLORS['accent_pink'])
        elif i % 3 == 1:
            draw.polygon([(x, y - 15), (x + 5, y - 5), (x + 15, y - 5),
                          (x + 7, y + 5), (x + 10, y + 15), (x, y + 8),
                          (x - 10, y + 15), (x - 7, y + 5), (x - 15, y - 5), (x - 5, y - 5)],
                         fill=COLORS['accent_yellow'])
        else:
            draw.ellipse([x - 12, y - 12, x + 12, y + 12], fill=COLORS['accent_blue'])
    
    # 金句（圆角背景框）
    draw_rounded_rectangle(draw, [WIDTH // 2 - 400, 350, WIDTH // 2 + 400, 550],
                          radius=50, fill=(255, 255, 255), outline=COLORS['accent_green'], width=5)
    
    quote1 = "血压不是数字游戏"
    quote2 = "是生活方式的反馈"
    
    bbox1 = draw.textbbox((0, 0), quote1, font=font_quote)
    draw.text(((WIDTH - bbox1[2] + bbox1[0]) // 2, 390), quote1, fill=COLORS['text_primary'], font=font_quote)
    
    bbox2 = draw.textbbox((0, 0), quote2, font=font_quote)
    draw.text(((WIDTH - bbox2[2] + bbox2[0]) // 2, 470), quote2, fill=COLORS['text_secondary'], font=font_quote)
    
    # 温馨提示
    tip = "稳住血压，从生活小细节开始"
    bbox_tip = draw.textbbox((0, 0), tip, font=font_brand)
    draw.text(((WIDTH - bbox_tip[2] + bbox_tip[0]) // 2, HEIGHT - 350), tip, fill=COLORS['accent_green'], font=font_brand)
    
    return img

def create_frame_09():
    """品牌收尾"""
    img = create_gradient_background(WIDTH, HEIGHT, COLORS['bg_top'], COLORS['bg_bottom'])
    draw = ImageDraw.Draw(img)
    font_brand = try_load_font(100)
    font_slogan = try_load_font(56)
    
    # 可爱卡通人物（鞠躬）
    draw_cute_character(draw, WIDTH // 2, HEIGHT // 2 + 50, scale=1.8, pose="happy", expression="smile")
    
    # 装饰（大量爱心和星星）
    for i in range(15):
        x = 80 + (i % 5) * 220
        y = 120 + (i // 5) * 120
        if i % 2 == 0:
            draw.polygon([(x - 15, y - 5), (x, y - 20), (x + 15, y - 5),
                          (x, y + 20)], fill=COLORS['accent_pink'])
        else:
            draw.polygon([(x, y - 15), (x + 5, y - 5), (x + 15, y - 5),
                          (x + 7, y + 5), (x + 10, y + 15), (x, y + 8),
                          (x - 10, y + 15), (x - 7, y + 5), (x - 15, y - 5), (x - 5, y - 5)],
                         fill=COLORS['accent_yellow'])
    
    # 品牌名（大圆角背景）
    draw_rounded_rectangle(draw, [WIDTH // 2 - 250, 450, WIDTH // 2 + 250, 650],
                          radius=60, fill=(255, 255, 255), outline=COLORS['accent_pink'], width=6)
    
    brand = "牧岚"
    bbox = draw.textbbox((0, 0), brand, font=font_brand)
    draw.text(((WIDTH - bbox[2] + bbox[0]) // 2, 490), brand, fill=COLORS['text_primary'], font=font_brand)
    
    # 口号
    slogan = "为健康发声"
    bbox_s = draw.textbbox((0, 0), slogan, font=font_slogan)
    draw.text(((WIDTH - bbox_s[2] + bbox_s[0]) // 2, 670), slogan, fill=COLORS['accent_green'], font=font_slogan)
    
    return img

# 生成所有帧
print("开始生成卡通可爱版视频帧...")

frames = [
    ("frame_001.png", create_frame_01(), 3),
    ("frame_002.png", create_frame_02(), 3),
    ("frame_003.png", create_frame_03(), 4),
    ("frame_004.png", create_frame_04(), 2),
    ("frame_005.png", create_frame_05(), 2),
    ("frame_006.png", create_frame_06(), 2),
    ("frame_007.png", create_frame_07(), 2),
    ("frame_008.png", create_frame_08(), 1.5),
    ("frame_009.png", create_frame_09(), 1.5),
]

for filename, img, duration in frames:
    filepath = os.path.join(OUTPUT_DIR, filename)
    img.save(filepath, "PNG")
    print(f"✓ 已生成 {filename} (持续{duration}秒)")

print(f"\n所有帧已保存到 {OUTPUT_DIR}")
print("下一步：使用 FFmpeg 合成视频")
