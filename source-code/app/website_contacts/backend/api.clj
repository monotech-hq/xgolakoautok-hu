
(ns app.website-contacts.backend.api
    (:require [app.website-contacts.backend.editor.lifecycles]
              [app.website-contacts.backend.editor.mutations]
              [app.website-contacts.backend.editor.resolvers]
              [app.website-contacts.backend.installer]
              [app.website-contacts.backend.handler.helpers :as handler.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.website-contacts.backend.handler.helpers
(def get-website-contacts handler.helpers/get-website-contacts)
