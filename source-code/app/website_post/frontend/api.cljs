
(ns app.website-post.frontend.api
    (:require [app.website-post.frontend.editor.effects]
              [app.website-post.frontend.editor.lifecycles]
              [app.website-post.frontend.lifecycles]
              [app.website-post.frontend.handler.queries :as handler.queries]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

; app.website-post.frontend.handler.queries
(def get-send-message-query handler.queries/get-send-message-query)
