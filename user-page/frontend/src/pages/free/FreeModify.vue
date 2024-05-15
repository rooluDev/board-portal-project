<template>
  <Navbar/>
  <div class="box">
    <v-row justify="center">
      <v-col cols="1"></v-col>
      <v-col cols="10">
        <h1 class="mb-4">자유 게시판</h1>
        <v-form @submit.prevent="modifyBoard">
          <v-select
              :items="categoryList"
              item-title="categoryName"
              item-value="categoryId"
              v-model="freeBoard.categoryId"
              solo>
          </v-select>
          <v-text-field
              label="제목"
              placeholder="제목을 입력하세요."
              v-model="freeBoard.title">
          </v-text-field>
          <v-textarea
              placeholder="내용을 입력하세요."
              rows="15"
              v-model="freeBoard.content">
          </v-textarea>
          <h3 class="mb-3">첨부 파일</h3>
          <div class="d-flex" v-for="(selectedFile,index) in selectedFileList" :key="index">
            <v-file-input
                label="첨부"
                :model-value="selectedFile"
                @change="fileSelected($event,index,selectedFile)"
                :accept="fileAcceptTypes">
            </v-file-input>
            <v-btn class="ml-2" style="height: 55px" @click="removeFileInput(index,file)">삭제</v-btn>
          </div>
          <div class="d-flex" v-for="(file,index) in fileList" :key="index">
            <v-file-input
                label="첨부"
                @change="fileSelected($event,index,file)"
                :accept="fileAcceptTypes">
            </v-file-input>
            <v-btn class="ml-2" style="height: 55px" @click="removeFileInput(index,file)">삭제</v-btn>
          </div>
          <v-btn class="d-block mb-4" @click="addFileInput">추가</v-btn>
          <div class=" d-flex justify-center align-center">
            <v-btn class="custom-btn" type="button">취소</v-btn>
            <v-btn class="custom-btn" type="submit">등록</v-btn>
          </div>
        </v-form>
      </v-col>
      <v-col cols="1"></v-col>
    </v-row>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import {computed, onMounted, ref, watch} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useStore} from "vuex";
import {fetchGetFreeBoard, fetchCheckFreeAuthor} from "@/api/freeBoardService";
import {downloadFile} from "@/api/fileService";
import {isValidFileSize} from "@/validator/validator";
import {fetchGetFileResource} from "@/api/imgaeService";

export default {
  components: {Navbar},
  setup() {
    const store = useStore();
    const accessToken = computed(() => store.getters.getAccessToken);

    const route = useRoute();
    const router = useRouter();
    const boardId = route.params.id;

    const freeBoard = ref({});
    const categoryList = ref([{categoryId: -1, categoryName: '카테고리 선택'}]);
    const fileList = ref([]);
    const deleteFileIdList = ref([]);

    const fileAcceptTypes = 'image/jpeg, image/jpg, image/gif, image/png, application/zip';
    const selectedFileList = ref([]);

    /**
     * 페이지 구성을 위한 데이터 로드
     */
    const getBoard = async () => {
      const res = await fetchGetFreeBoard(boardId);
      freeBoard.value = res.freeBoard;
      fileList.value = res.fileList;
      res.categoryList.forEach((category) => {
        categoryList.value.push(category);
      })
    }

    const getSelectedFileBlobResource = async () => {
      for (const file of fileList.value) {
        const blob = await fetchGetFileResource(file.fileId);
        selectedFileList.value.push(new File([blob], file.originalName));
      }
    }

    onMounted(async () => {
      await getBoard();
      // checkAuthor();
      await getSelectedFileBlobResource();
    })

    watch(accessToken, async () => {
      await checkAuthor();
    })

    const checkAuthor = async () => {
      try {
        await fetchCheckFreeAuthor(boardId);
      } catch (error) {
        alert("로그인이 필요합니다.");
        goToList();
      }
    }

    /**
     * 게시물 수정
     */
    const modifyBoard = async () => {
      try {
        fileList.value = fileList.value.filter(file => file != null);

        console.log(fileList.value);
        // console.log(deleteFileIdList.value);
        // freeBoardValidator(freeBoard.value);

        const formData = new FormData();
        fileList.value.forEach((file) => {
          if (file instanceof File){
            console.log(1);
            formData.append('file', file);
          }

        })
        formData.append('deleteFileIdList', deleteFileIdList.value);
        formData.append('categoryId', freeBoard.value.categoryId);
        formData.append('title', freeBoard.value.title);
        formData.append('content', freeBoard.value.content);
        // await fetchModifyFreeBoard(boardId, formData);
        // goToList();

      } catch (error) {
        alert(error.message);
      }
    }

    /**
     * 파일 다운로드
     *
     * @param fileId fileId PK
     */
    const download = async (fileId) => {
      const res = await downloadFile(fileId);
      let fileName = '';

      for (const file of fileList.value) {
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

    const fileSelected = (event, index, existedFile) => {
      if (existedFile) {
        deleteFileIdList.value.push(existedFile.fileId);
      }

      const addedFile = event.target.files[0];

      const isValidFile = addedFile && isValidFileSize(addedFile.size, 2 * 1024 * 1024);

      if (isValidFile) {
        fileList.value[index] = addedFile;
      } else {
        fileList.value.splice(index, 1);
      }
    }

    const addFileInput = () => {
      if (fileList.value.length < 5) {
        fileList.value.push(null);
      } else {
        alert("파일은 최대 5개까지만 등록 가능합니다.");
      }
    }

    const removeFileInput = (index, file) => {
      console.log(fileList.value);
      console.log(index);
      if (file) {
        deleteFileIdList.value.push(file.fileId);
        selectedFileList.value.splice(index, 1);
      }
      fileList.value.splice(index, 1);
    }

    const goToList = () => {
      router.push({
        name: 'Free-List',
        query: route.query
      })
    }

    return {
      freeBoard,
      categoryList,
      fileList,
      fileAcceptTypes,
      getBoard,
      goToList,
      modifyBoard,
      fileSelected,
      addFileInput,
      removeFileInput,
      download,
      selectedFileList
    }
  }
}
</script>

<style scoped>
.box {
  margin: 50px 100px 10px;
}

.custom-btn {
  margin: 30px 10px 20px;
  background-color: black;
  color: white;
  width: 200px;
}
</style>
