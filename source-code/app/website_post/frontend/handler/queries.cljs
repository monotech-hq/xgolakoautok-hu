
(ns app.website-post.frontend.handler.queries)

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn get-send-message-query
  ; @param (map) query-props
  ;  {:visitor-email-address (string)
  ;   :visitor-message (string)
  ;   :visitor-name (string)}
  ;
  ; @usage
  ;  (r get-send-message-query db {...})
  ;
  ; @return (vector)
  [db [_ {:keys [visitor-email-address visitor-message visitor-name]}]]
  [`(~(symbol "website-post.handler/send-message!") ~{:visitor-message       visitor-message
                                                      :visitor-email-address visitor-email-address
                                                      :visitor-name          visitor-name})])
