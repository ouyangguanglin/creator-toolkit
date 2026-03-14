// CreatorToolkit - 前端逻辑

document.addEventListener('DOMContentLoaded', () => {
    // 标签页切换
    const tabs = document.querySelectorAll('.tab');
    const tabContents = document.querySelectorAll('.tab-content');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tabContents.forEach(c => c.classList.remove('active'));
            
            tab.classList.add('active');
            document.getElementById(tab.dataset.tab).classList.add('active');
        });
    });

    // 图片压缩功能
    initCompressFeature();
    
    // 格式转换功能
    initConvertFeature();
    
    // 发布功能
    initPublishFeature();
});

// 图片压缩功能
function initCompressFeature() {
    const dropZone = document.getElementById('dropZone');
    const fileInput = document.getElementById('fileInput');
    const qualitySlider = document.getElementById('quality');
    const qualityValue = document.getElementById('qualityValue');
    const compressBtn = document.getElementById('compressBtn');
    const previewArea = document.getElementById('previewArea');
    
    let selectedFiles = [];

    // 质量滑块
    qualitySlider.addEventListener('input', () => {
        qualityValue.textContent = qualitySlider.value + '%';
    });

    // 拖拽上传
    dropZone.addEventListener('click', () => fileInput.click());
    
    dropZone.addEventListener('dragover', (e) => {
        e.preventDefault();
        dropZone.classList.add('dragover');
    });
    
    dropZone.addEventListener('dragleave', () => {
        dropZone.classList.remove('dragover');
    });
    
    dropZone.addEventListener('drop', (e) => {
        e.preventDefault();
        dropZone.classList.remove('dragover');
        selectedFiles = Array.from(e.dataTransfer.files).filter(f => f.type.startsWith('image/'));
        showPreview(selectedFiles, previewArea);
    });
    
    fileInput.addEventListener('change', () => {
        selectedFiles = Array.from(fileInput.files).filter(f => f.type.startsWith('image/'));
        showPreview(selectedFiles, previewArea);
    });

    // 压缩按钮
    compressBtn.addEventListener('click', async () => {
        if (selectedFiles.length === 0) {
            alert('请先选择图片！');
            return;
        }

        compressBtn.disabled = true;
        compressBtn.textContent = '压缩中...';

        const quality = parseInt(qualitySlider.value) / 100;
        const maxWidth = parseInt(document.getElementById('maxWidth').value) || 1920;
        const maxHeight = parseInt(document.getElementById('maxHeight').value) || 1080;

        try {
            const compressedImages = [];
            
            for (const file of selectedFiles) {
                const compressed = await compressImage(file, quality, maxWidth, maxHeight);
                compressedImages.push({
                    name: file.name,
                    originalSize: file.size,
                    compressedSize: compressed.blob.size,
                    blob: compressed.blob,
                    url: compressed.url
                });
            }

            showCompressedResult(compressedImages, previewArea);
        } catch (error) {
            alert('压缩失败：' + error.message);
        } finally {
            compressBtn.disabled = false;
            compressBtn.textContent = '开始压缩';
        }
    });
}

// 压缩图片
async function compressImage(file, quality, maxWidth, maxHeight) {
    return new Promise((resolve, reject) => {
        const img = new Image();
        const reader = new FileReader();

        reader.onload = (e) => {
            img.src = e.target.result;
        };

        img.onload = () => {
            const canvas = document.createElement('canvas');
            let width = img.width;
            let height = img.height;

            // 等比例缩放
            if (width > maxWidth) {
                height = (maxWidth / width) * height;
                width = maxWidth;
            }
            if (height > maxHeight) {
                width = (maxHeight / height) * width;
                height = maxHeight;
            }

            canvas.width = width;
            canvas.height = height;

            const ctx = canvas.getContext('2d');
            ctx.drawImage(img, 0, 0, width, height);

            canvas.toBlob((blob) => {
                if (blob) {
                    resolve({
                        blob,
                        url: URL.createObjectURL(blob)
                    });
                } else {
                    reject(new Error('压缩失败'));
                }
            }, 'image/jpeg', quality);
        };

        img.onerror = () => reject(new Error('图片加载失败'));
        reader.readAsDataURL(file);
    });
}

// 显示预览
function showPreview(files, container) {
    container.innerHTML = files.map((file, index) => `
        <div class="image-card">
            <img src="${URL.createObjectURL(file)}" alt="${file.name}">
            <div class="info">
                <div>${file.name}</div>
                <div>${formatFileSize(file.size)}</div>
            </div>
        </div>
    `).join('');
}

// 显示压缩结果
function showCompressedResult(images, container) {
    container.innerHTML = images.map((img, index) => {
        const savings = ((1 - img.compressedSize / img.originalSize) * 100).toFixed(1);
        return `
            <div class="image-card">
                <img src="${img.url}" alt="${img.name}">
                <div class="info">
                    <div>${img.name}</div>
                    <div>原始：${formatFileSize(img.originalSize)}</div>
                    <div>压缩：${formatFileSize(img.compressedSize)}</div>
                    <div style="color: ${savings > 0 ? 'green' : 'red'}">
                        节省：${savings}%
                    </div>
                </div>
                <a href="${img.url}" download="compressed-${img.name}" class="download-btn">下载</a>
            </div>
        `;
    }).join('');
}

// 格式转换功能
function initConvertFeature() {
    const dropZone = document.getElementById('convertDropZone');
    const fileInput = document.getElementById('convertFileInput');
    const convertBtn = document.getElementById('convertBtn');
    const previewArea = document.getElementById('convertPreviewArea');
    const targetFormat = document.getElementById('targetFormat');
    
    let selectedFiles = [];

    dropZone.addEventListener('click', () => fileInput.click());
    
    dropZone.addEventListener('dragover', (e) => {
        e.preventDefault();
        dropZone.classList.add('dragover');
    });
    
    dropZone.addEventListener('dragleave', () => {
        dropZone.classList.remove('dragover');
    });
    
    dropZone.addEventListener('drop', (e) => {
        e.preventDefault();
        dropZone.classList.remove('dragover');
        selectedFiles = Array.from(e.dataTransfer.files).filter(f => f.type.startsWith('image/'));
        showPreview(selectedFiles, previewArea);
    });
    
    fileInput.addEventListener('change', () => {
        selectedFiles = Array.from(fileInput.files).filter(f => f.type.startsWith('image/'));
        showPreview(selectedFiles, previewArea);
    });

    convertBtn.addEventListener('click', async () => {
        if (selectedFiles.length === 0) {
            alert('请先选择图片！');
            return;
        }

        convertBtn.disabled = true;
        convertBtn.textContent = '转换中...';

        try {
            const convertedImages = [];
            
            for (const file of selectedFiles) {
                const converted = await convertImage(file, targetFormat.value);
                convertedImages.push({
                    name: file.name,
                    originalSize: file.size,
                    convertedSize: converted.blob.size,
                    blob: converted.blob,
                    url: converted.url
                });
            }

            showConvertedResult(convertedImages, previewArea);
        } catch (error) {
            alert('转换失败：' + error.message);
        } finally {
            convertBtn.disabled = false;
            convertBtn.textContent = '开始转换';
        }
    });
}

// 转换图片格式
async function convertImage(file, format) {
    return new Promise((resolve, reject) => {
        const img = new Image();
        const reader = new FileReader();

        reader.onload = (e) => {
            img.src = e.target.result;
        };

        img.onload = () => {
            const canvas = document.createElement('canvas');
            canvas.width = img.width;
            canvas.height = img.height;

            const ctx = canvas.getContext('2d');
            ctx.drawImage(img, 0, 0);

            canvas.toBlob((blob) => {
                if (blob) {
                    resolve({
                        blob,
                        url: URL.createObjectURL(blob)
                    });
                } else {
                    reject(new Error('转换失败'));
                }
            }, format);
        };

        img.onerror = () => reject(new Error('图片加载失败'));
        reader.readAsDataURL(file);
    });
}

// 显示转换结果
function showConvertedResult(images, container) {
    container.innerHTML = images.map((img) => `
        <div class="image-card">
            <img src="${img.url}" alt="${img.name}">
            <div class="info">
                <div>${img.name}</div>
                <div>原始：${formatFileSize(img.originalSize)}</div>
                <div>转换后：${formatFileSize(img.convertedSize)}</div>
            </div>
            <a href="${img.url}" download="converted-${img.name}" class="download-btn">下载</a>
        </div>
    `).join('');
}

// 发布功能
function initPublishFeature() {
    const publishBtn = document.getElementById('publishBtn');
    const publishResult = document.getElementById('publishResult');

    publishBtn.addEventListener('click', async () => {
        const title = document.getElementById('postTitle').value.trim();
        const content = document.getElementById('postContent').value.trim();
        const submolt = document.getElementById('submolt').value;
        const apiKey = document.getElementById('apiKey').value.trim();

        if (!title || !content) {
            showAlert('请填写标题和内容', 'error');
            return;
        }

        if (!apiKey) {
            showAlert('请填写 API Key', 'error');
            return;
        }

        publishBtn.disabled = true;
        publishBtn.textContent = '发布中...';

        try {
            const response = await fetch('https://www.moltbook.com/api/v1/posts', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${apiKey}`
                },
                body: JSON.stringify({
                    title,
                    content,
                    submolt
                })
            });

            const data = await response.json();

            if (data.success) {
                const postId = data.post.id;
                showAlert(`发布成功！帖子 ID: ${postId}\n链接：https://www.moltbook.com/posts/${postId}`, 'success');
                
                // 清空表单
                document.getElementById('postTitle').value = '';
                document.getElementById('postContent').value = '';
            } else {
                showAlert(`发布失败：${data.message || '未知错误'}`, 'error');
            }
        } catch (error) {
            showAlert(`发布失败：${error.message}`, 'error');
        } finally {
            publishBtn.disabled = false;
            publishBtn.textContent = '发布';
        }
    });
}

// 显示结果
function showAlert(message, type) {
    const resultDiv = document.getElementById('publishResult');
    resultDiv.textContent = message;
    resultDiv.className = type;
    resultDiv.style.display = 'block';

    setTimeout(() => {
        resultDiv.style.display = 'none';
    }, 10000);
}

// 格式化文件大小
function formatFileSize(bytes) {
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
    return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
}
