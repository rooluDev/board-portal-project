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
          <div class="d-flex" v-for="(existFile,index) in existFileList" :key="index">
            <v-file-input
                label="첨부"
                :model-value="existFile.file"
                @change="fileSelected($event,index,existFile.fileId)"
                :accept="fileAcceptTypes">
            </v-file-input>
            <v-btn class="ml-2" style="height: 55px" @click="removeFileInput(index,existFile.fileId)">삭제</v-btn>
          </div>
          <v-btn class="d-block mb-4" @click="addFileInput">추가</v-btn>
          <div class=" d-flex justify-center align-center">
            <v-btn class="custom-btn" @click="goToList" type="button">취소</v-btn>
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
import {fetchGetFreeBoard, fetchCheckFreeAuthor, fetchModifyFreeBoard} from "@/api/freeBoardService";
import {freeBoardValidator, isValidFileSize} from "@/validator/validator";
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
    const existFileList = ref([]);
    const addFileList = ref([]);
    const deleteFileIdList = ref([]);
    const constraint = {
      title: {
        maxLength: 99,
        minLength: 1
      },
      content: {
        maxLength: 3999,
        minLength: 1
      }
    }

    const fileAcceptTypes = 'image/jpeg, image/jpg, image/gif, image/png, application/zip';

    /**
     * 페이지 구성을 위한 데이터 로드
     */
    const getBoard = async () => {
      const res = await fetchGetFreeBoard(boardId);
      freeBoard.value = res.freeBoard;
      res.fileList.forEach(file => {
        existFileList.value.push({
          fileId: file.fileId,
          originalName: file.originalName,
          file: null
        });
      });
      res.categoryList.forEach((category) => {
        categoryList.value.push(category);
      })
    }

    const getExistFileBlobResource = async () => {
      for (const existedFile of existFileList.value) {
        try {
          const blob = await fetchGetFileResource(existedFile.fileId);
          existedFile.file = new File([blob], existedFile.originalName);
        } catch (error) {
          existedFile.file = null;
        }
      }
    }

    onMounted(async () => {
      await getBoard();
      // checkAuthor();
      await getExistFileBlobResource();
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
      if (!confirm("수정 하시겠습니까?")){
        return;
      }
      try {
        addFileList.value = addFileList.value.filter(file => file != null);
        freeBoardValidator(freeBoard.value, constraint);

        const formData = new FormData();
        addFileList.value.forEach((file) => {
          if (file instanceof File) {
            formData.append('file', file);
          }
        })
        formData.append('categoryId', freeBoard.value.categoryId);
        formData.append('title', freeBoard.value.title);
        formData.append('content', freeBoard.value.content);
        formData.append('deleteFileIdList', deleteFileIdList.value);
        await fetchModifyFreeBoard(boardId, formData);
        alert("수정 되었습니다.");
        goToList();
      } catch (error) {
        alert(error.message);
      }
    }

    const fileSelected = (event, index, existFileId) => {
      const selectedFile = event.target.files[0];

      const isValidFile = selectedFile && isValidFileSize(selectedFile.size, 2 * 1024 * 1024);

      if (existFileId) {
        deleteFileIdList.value.push(existFileId);
      }
      if (isValidFile) {
        existFileList.value[index].file = selectedFile;
        addFileList.value.push(selectedFile);
      }
    }

    const addFileInput = () => {
      if (existFileList.value.length < 5) {
        existFileList.value.push({
          fileId: null,
          originalName: null,
          file: null
        });
      } else {
        alert("파일은 최대 5개까지만 등록 가능합니다.");
      }
    }

    const removeFileInput = (index, fileId) => {
      if (fileId) {
        deleteFileIdList.value.push(fileId);
      }
      existFileList.value.splice(index, 1);
      addFileList.value.splice(index, 1);
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
      addFileList,
      fileAcceptTypes,
      existFileList,
      getBoard,
      goToList,
      modifyBoard,
      fileSelected,
      addFileInput,
      removeFileInput
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
