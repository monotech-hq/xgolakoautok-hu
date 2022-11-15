
(ns app.website-post.backend.handler.side-effects
    (:require [app.website-post.backend.handler.helpers :as handler.helpers]
              [gestures.api                             :as gestures]
              [plugins.postal.api                       :as postal]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn send-message!
  ; @param (map) request
  ; @param (map) message-props
  ;  {:visitor-email-address (string)
  ;   :visitor-message (string)
  ;   :visitor-name (string)}
  [request {:keys [visitor-email-address visitor-message visitor-name] :as message-props}]
  (let [website-post  (handler.helpers/get-website-post)
        email-body    (gestures/resolve-variable (:email-body website-post)
                                                 [[visitor-email-address "@reply-address" "@valasz-cim"]
                                                  [visitor-name          "@sender-name"   "@felado-neve"]
                                                  [visitor-message       "@message"       "@uzenet"]])
        email-subject (gestures/resolve-variable (:email-subject website-post)
                                                 [[visitor-email-address "@reply-address" "@valasz-cim"]
                                                  [visitor-name          "@sender-name"   "@felado-neve"]])]
       (postal/send-email! {:body          email-body
                            :subject       email-subject
                            :email-address (:receiver-email-address website-post)
                            :host          (:email-server-address   website-post)
                            :password      (:account-password       website-post)
                            :port          (:email-server-port      website-post)
                            :username      (:account-username       website-post)
                            :sender-name   "Website Post"})))
