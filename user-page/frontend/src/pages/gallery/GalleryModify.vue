<template>
  <Navbar/>
  <div class="box">
    <v-col cols="1"></v-col>
    <v-col cols="10">
      <h1 class="mb-4">갤러리</h1>
      <v-form @submit.prevent="modifyBoard">
        <v-select
            :items="categoryList"
            item-title="categoryName"
            item-value="categoryId"
            v-model="galleryBoard.categoryId"
            solo
        ></v-select>
        <v-text-field
            label="제목"
            placeholder="제목을 입력하세요."
            v-model="galleryBoard.title">
        </v-text-field>
        <v-textarea
            placeholder="내용을 입력하세요."
            rows="15"
            v-model="galleryBoard.content">
        </v-textarea>
        <h3 class="mb-3">첨부 파일</h3>
        <div class="d-flex" v-for="(file, index) in fileList" :key="index">
          <v-img
            :src="filePreviews[index] || imageUrls[file.fileId]"
            :max-width="60"
            :height="60"
            :aspect-ratio="1"
            cover
          ></v-img>
          <v-file-input
              label="첨부"
              :accept="fileAcceptTypes"
              :model-value="selectedFileList[index]"
              prepend-icon="none"
              @change="fileSelected($event, index, file.fileId)"
          ></v-file-input>
          <v-btn class="ml-2" style="height: 55px" @click="removeFileInput($event, index, file.fileId)">삭제</v-btn>
        </div>
        <v-btn class="d-block mb-4" @click="addFileInput">추가</v-btn>
        <div class=" d-flex justify-center align-center">
          <v-btn class="custom-btn" type="button">취소</v-btn>
          <v-btn class="custom-btn" type="submit">등록</v-btn>
        </div>
      </v-form>
    </v-col>
    <v-col cols="1"></v-col>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import store from "@/store";
import {computed, onMounted, ref, watch} from "vue";
import {useRoute, useRouter} from "vue-router";
import {fetchGetGalleryBoard, fetchModifyGalleryBoard, fetchCheckGalleryAuthor} from "@/api/galleryBoardService";
import {downloadFile} from "@/api/fileService";
import {fetchGetFileResource} from "@/api/imgaeService";
import { isValidFileSize} from "@/validator/validator";

export default {
  components: {Navbar},
  setup() {
    const route = useRoute();
    const router = useRouter();
    const accessToken = computed(() => store.getters.getAccessToken);
    const boardId = route.params.id;

    const galleryBoard = ref({});
    const categoryList = ref([{categoryId: -1, categoryName: '카테고리 선택'}]);
    const fileList = ref([]);
    const deleteFileIdList = ref([]);
    const imageUrls = ref({});
    const filePreviews = ref([]);
    const fileAcceptTypes = 'image/jpeg, image/jpg, image/gif, image/png';
    const selectedFileList = ref([]);


    /**
     * 페이지 구성을 위한 데이터 로드
     */
    const getBoard = async () => {
      const res = await fetchGetGalleryBoard(boardId);
      galleryBoard.value = res.galleryBoard;
      fileList.value = res.fileList;
      res.categoryList.forEach((category) => {
        categoryList.value.push(category);
      })
    }

    /**
     *  미리보기용 파일 이미지 리소스 가져오기
     */
    const getFileResource = async () => {
      for (const file of fileList.value) {
        try {
          const imageBlob = await fetchGetFileResource(file.fileId);
          selectedFileList.value.push(new File([imageBlob], file.originalName));
          imageUrls.value[file.fileId] = URL.createObjectURL(imageBlob);
        } catch (error) {
          imageUrls.value[file.fileId] = null;
        }
      }
    };

    onMounted(async () => {
      await getBoard();
      await getFileResource();
    })

    watch(accessToken, async (newToken) => {
      if (newToken) {
        try {
          await fetchCheckGalleryAuthor(boardId);
        } catch (error) {
          alert("로그인이 필요합니다.");
          goToList();
        }
      } else {
        alert("로그인이 필요합니다.");
        goToList();
      }
    })

    /**
     * 게시물 수정
     */
    const modifyBoard = async () => {
      // try {
      //   galleryBoardValidator(galleryBoard.value);
      //   validateFileLengthForModify(fileList.value, deleteFileIdList.value, 1, 5);
      // } catch (error) {
      //   alert(error.message);
      //   return;
      // }
      const formData = new FormData();
      fileList.value.forEach((file) => {
        if (file instanceof File) {
          formData.append('file', file);
        }
      })
      formData.append("deleteFileIdList", deleteFileIdList.value);
      formData.append('categoryId', galleryBoard.value.categoryId);
      formData.append('title', galleryBoard.value.title);
      formData.append('content', galleryBoard.value.content);
      try {
        await fetchModifyGalleryBoard(boardId, formData);
        goToList();
      } catch (error) {
        await router.push({
          name: 'Error'
        })
      }
    };

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


    const fileSelected = (event, index, fileId) => {
      const file = event.target.files[0];
      const isValidFile = file && isValidFileSize(file.size, 1 * 1024 * 1024);

      if (isValidFile) {
        const reader = new FileReader();
        reader.onload = (e) => {
          filePreviews.value[index] = e.target.result;
        };
        fileList.value.splice(index, 1, file);
        reader.readAsDataURL(file);
        deleteFileIdList.value.push(fileId);
      } else {
        if (fileId) {
          deleteFileIdList.value.push(fileId);
          fileList.value.splice(index, 1);
          filePreviews.value.splice(index, 1);
        }
      }
    }

    const addFileInput = () => {
      if (fileList.value.length < 5) {
        fileList.value.push({});
      } else {
        alert("파일은 최대 5개까지만 등록 가능합니다.");
      }
    }

    const removeFileInput = (index, fileId) => {
      console.log(filePreviews.value);
      if (fileId) {
        deleteFileIdList.value.push(fileId);
        filePreviews.value.splice(fileId, 1);
      }
      fileList.value.splice(index, 1);
      filePreviews.value.splice(index, 1);
    }

    const goToList = () => {
      router.push({
        name: 'Gallery-List',
        query: route.query
      })
    }

    return {
      galleryBoard,
      categoryList,
      fileList,
      imageUrls,
      filePreviews,
      fileAcceptTypes,
      selectedFileList,
      getBoard,
      goToList,
      modifyBoard,
      fileSelected,
      addFileInput,
      removeFileInput,
      download
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