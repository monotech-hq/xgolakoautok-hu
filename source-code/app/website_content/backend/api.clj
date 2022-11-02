
(ns app.website-content.backend.api
    (:require [app.website-content.backend.editor.lifecycles]
              [app.website-content.backend.editor.mutations]
              [app.website-content.backend.editor.resolvers]
              [app.website-content.backend.handler.helpers :as handler.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.website-content.backend.handler.helpers
(def get-website-content handler.helpers/get-website-content)
