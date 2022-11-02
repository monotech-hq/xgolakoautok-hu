
(ns app.website-config.backend.api
    (:require [app.website-config.backend.editor.lifecycles]
              [app.website-config.backend.editor.mutations]
              [app.website-config.backend.editor.resolvers]
              [app.website-config.backend.handler.helpers :as handler.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.website-config.backend.handler.helpers
(def get-website-config handler.helpers/get-website-config)
