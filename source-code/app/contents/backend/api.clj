
(ns app.contents.backend.api
    (:require [app.contents.backend.editor.lifecycles]
              [app.contents.backend.editor.mutations]
              [app.contents.backend.editor.resolvers]
              [app.contents.backend.handler.lifecycles]
              [app.contents.backend.handler.resolvers]
              [app.contents.backend.installer]
              [app.contents.backend.lister.lifecycles]
              [app.contents.backend.lister.mutations]
              [app.contents.backend.lister.resolvers]
              [app.contents.backend.preview.lifecycles]
              [app.contents.backend.preview.resolvers]
              [app.contents.backend.selector.lifecycles]
              [app.contents.backend.viewer.lifecycles]
              [app.contents.backend.viewer.mutations]
              [app.contents.backend.viewer.resolvers]
              [app.contents.backend.handler.helpers :as handler.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.contents.backend.handler.helpers
(def get-content-body handler.helpers/get-content-body)
(def fill-data        handler.helpers/fill-data)
