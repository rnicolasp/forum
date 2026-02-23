export default {
  canEditTopic (topic) {
    return true
  },

  canDeleteTopic (topic) {
    return true
  },

  canEditReply (reply, categorySlug) {
    return true
  },

  canDeleteReply (reply, categorySlug) {
    return true
  },

  checkPermissions ({ permissions, isOwner = null, scope = null }) {
    return true
  },

  can (permissions, scope = null) {
      return true
  }
}
