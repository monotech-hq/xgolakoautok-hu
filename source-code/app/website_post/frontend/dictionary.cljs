
(ns app.website-post.frontend.dictionary)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:email-body-helper
           {:en "The body of the email could contain the following items: @message @reply-address @sender-name"
            :hu "Az email törzse a következő elemeket tartalmazhatja: @felado-neve @uzenet @valasz-cim"}
           :email-body-placeholder
           {:en "@sender-name (@reply-address) sent you the following message: @message"
            :hu "@felado-neve (@valasz-cim) a következő üzenetet küldte: @uzenet"}
           :email-subject-helper
           {:en "The subject of the email could contain the following items: @reply-address @sender-name"
            :hu "Az email fejléce a következő elemeket tartalmazhatja: @felado-neve @valasz-cim"}
           :email-subject-placeholder
           {:en "Website post: @sender-name sent you a message"
            :hu "Webhely posta: @felado-neve üzenetet küldött"}
           :website-post
           {:en "Website post"
            :hu "Webhely posta"}})
