<template>
  <div v-for="(file,index) in fileList" :key="index">
    <span @click="download(file.fileId)">{{ file.originalName}}</span>
  </div>
</template>

<script>
import {downloadFile} from '@/api/fileService';
export default {
  props:{
    fileList:{
      type:Array,
      required:false
    }
  },
  setup(props){
    const download = async (fileId) => {
      const res = await downloadFile(fileId);
      let fileName = '';

      for (const file of props.fileList) {
        if (file.fileId == fileId) {
          fileName = file.originalName;
        }
      }
      // download object 설정
      const url = window.URL.createObjectURL(new Blob([res]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `${fileName}`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    }
    return{
      download
    }
  }
}
</script>

<style>

</style>