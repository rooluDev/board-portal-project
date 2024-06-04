<template>
  <v-container class="box">
    <v-form @submit.prevent="addComment" v-show="!$props.isDeleted" class="d-flex align-center">
      <v-textarea
          v-model="content"
          placeholder="댓글을 입력해 주세요."
          outlined
          rows="1"
          auto-grow
      ></v-textarea>
      <v-btn class="custom-btn" type="submit">등록</v-btn>
    </v-form>
    <v-list>
      <v-list-item v-for="(comment, index) in commentList" :key="index">
        <div class="d-flex justify-space-between">
          <v-list-item-title class="justify-start">
            {{ comment.adminName || comment.memberName }}
            <span>{{ parseStringByFormat(comment.createdAt, 'YYYY.MM.DD HH:mm') }}</span>
          </v-list-item-title>
          <v-list-item-action class="justify-end mr-2" style="font-size: 15px">
                <span v-if="memberId == comment.authorId && !$props.isDeleted"
                      class="delete"
                      @click="deleteComment(comment.commentId, index)">삭제</span>
          </v-list-item-action>
        </div>
        <v-list-item-title class="comment-content">
          {{ comment.content }}
        </v-list-item-title>
        <v-divider></v-divider>
      </v-list-item>
    </v-list>
  </v-container>
</template>


<script>
import store from "@/store";
import {computed, getCurrentInstance, ref, watch} from "vue";
import {parseStringByFormat} from "@/utils/searchConditionUtils";
import {fetchGetMember,} from "@/api/memberService";
import {fetchDeleteComment} from "@/api/commentService";

export default {
  props: {
    commentList: {
      type: Array,
      required: true
    },
    isDeleted: {
      type: Boolean,
      required: true
    }
  },
  emits: ['submit'],
  setup(props) {
    const {emit} = getCurrentInstance();
    const content = ref("");
    const memberId = ref(null);
    const accessToken = computed(() => store.getters.getAccessToken);

    watch(accessToken, async (newToken) => {
      if (newToken) {
        const res = await fetchGetMember();
        memberId.value = res.memberId;
      } else {
        memberId.value = null;
      }
    }, {immediate: true});

    /**
     * 댓글추가
     */
    const addComment = () => {
      if (accessToken.value && confirm("등록 하시겠습니까?")) {
        emit('submit', content.value);
        content.value = '';
      } else {
        alert("로그인이 필요합니다.");
      }
    }

    const deleteComment = async (commentId, index) => {
      if (confirm("삭제 하시겠습니까?")){
        await fetchDeleteComment(commentId);
        props.commentList.splice(index, 1);
      }
    }

    return {
      content,
      memberId,
      addComment,
      parseStringByFormat,
      deleteComment
    }
  }
}
</script>

<style scoped>
.custom-btn {
  margin-left: 15px;
  margin-bottom: 20px;
  background-color: black;
  color: white;
  width: 100px;
  height: 48px;
}

.delete {
  cursor: pointer;
  text-decoration: none;
}

.delete:hover {
  text-decoration: underline;
}

.comment-content {
  white-space: pre-wrap;
  overflow-wrap: break-word;
  word-wrap: break-word;
  margin-top: 5px;
  margin-bottom: 5px;
}

.box {
  border: 1px solid black;
  border-radius: 12px;
}
</style>