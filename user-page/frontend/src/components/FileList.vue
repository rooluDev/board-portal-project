<template>
  <v-list>
    <h3 class="ml-4">첨부 파일</h3>
    <v-list-item v-for="(file, index) in fileList" :key="index" class="download-item">
      <div class="d-inline-flex align-center justify-start clickable-area" @click="download(file.fileId)">
        <v-icon>mdi-file-download</v-icon>
        <v-list-item-title class="ml-2">{{ file.originalName }}</v-list-item-title>
      </div>
    </v-list-item>
  </v-list>
</template>

<script>
import { downloadFile } from '@/api/fileService';

export default {
  props: {
    fileList: {
      type: Array,
      required: true
    }
  },
  setup(props) {
    const download = async (fileId) => {
      const res = await downloadFile(fileId);
      const fileName = props.fileList.find(f => f.fileId === fileId)?.originalName;
      if (fileName) {
        const url = window.URL.createObjectURL(new Blob([res.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        link.remove();
      }
    };

    return { download };
  }
}
</script>

<style scoped>
.clickable-area {
  transition: background-color 0.3s ease;
  cursor: pointer;
}

.clickable-area:hover {
  background-color: #f5f5f5;
}

.ml-2 {
  margin-left: 8px;
}
</style>
